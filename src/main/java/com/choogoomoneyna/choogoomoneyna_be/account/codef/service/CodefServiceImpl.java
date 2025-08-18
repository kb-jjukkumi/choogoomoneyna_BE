package com.choogoomoneyna.choogoomoneyna_be.account.codef.service;

import com.choogoomoneyna.choogoomoneyna_be.account.codef.dto.*;
import com.choogoomoneyna.choogoomoneyna_be.account.codef.vo.AccountVO;
import com.choogoomoneyna.choogoomoneyna_be.account.codef.vo.TransactionVO;
import com.choogoomoneyna.choogoomoneyna_be.account.db.dto.TransactionItemDto;
import com.choogoomoneyna.choogoomoneyna_be.account.db.mapper.AccountMapper;
import com.choogoomoneyna.choogoomoneyna_be.account.db.service.TransactionConverter;
import com.choogoomoneyna.choogoomoneyna_be.user.mapper.UserMapper;
import com.choogoomoneyna.choogoomoneyna_be.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CodefServiceImpl implements CodefService {

    private final CodefApiRequester codefApiRequester;
    private final UserMapper userMapper;
    private final AccountMapper accountMapper;

    @Override
    public List<AccountResponseDto> addAccount(Long userId, AccountRequestDto accountRequestDto) throws Exception {

        //1. 유저 정보 추출
        UserVO userVO = userMapper.findById(userId);
        String connectedId = userVO.getConnectedId();

        // 2. connectedId 처리 (없을 경우 등록 api로 연결)
        if (connectedId == null) {
            connectedId = codefApiRequester.registerConnectedId(accountRequestDto);
            userMapper.updateConnectedId(userId, connectedId);
        } else {
            connectedId = codefApiRequester.addConnectedId(connectedId, accountRequestDto);
        }

        // 3. codef api로 계좌 리스트 가져오기
        List<AccountResponseDto> accountList = codefApiRequester.getAccountList(accountRequestDto, connectedId);
        System.out.println(accountList);

        if (accountList == null || accountList.isEmpty()) {
            log.info("None account to added.");
            return List.of();
        }

        // 5. 계좌 정보를 AccountVo 리스트로 매핑
        List<AccountVO> accountVOList = accountList.stream()
                .map(accountInfo -> mapToAccountVO(userId, accountInfo))
                .toList();

        // 6. accounts db 저장
        accountMapper.insertAccount(accountVOList);
        log.info("{} accounts added.", accountVOList.size());

        return accountList;
    }





    @Override
    public AccountResponseDto updateAccountOne(Long userId, AccountUpdateRequestDto accountUpdateRequestDto) throws Exception {
        //1. 유저 정보 추출
        UserVO userVO = userMapper.findById(userId);
        String connectedId = userVO.getConnectedId();
        log.debug("connectedId: {}", connectedId);
        String bankId = accountUpdateRequestDto.getBankId();
        String accountNum = accountUpdateRequestDto.getAccountNum();
        log.info("조회하려는 계좌번호 {}", accountNum);

        // 2. codef api로 계좌 리스트 가져오기
        List<AccountResponseDto> accountList = codefApiRequester.getAccountOne(bankId, connectedId);

        if (accountList == null || accountList.isEmpty()) {
            System.out.println("accountList is empty."+ accountList);
            log.info("None account added.");
            throw new Exception("check id/password.");
        }

        //3. 동일 계좌 찾기
        for(AccountResponseDto accountResponseDto : accountList) {
            log.info("accountResponseDto: {}", accountResponseDto);
            if (accountResponseDto.getAccountNum().equals(accountNum)) {
                AccountVO accountVO = accountMapper.findByAccountNum(accountNum);
                if (accountVO == null) {
                    throw new Exception("Account not found in DB.");
                }

                //5. 잔액 비교 후 다르면 업데이트
                if(!accountVO.getAccountBalance().equals(accountResponseDto.getAccountBalance())) {
                    accountVO.setAccountBalance(accountResponseDto.getAccountBalance());

                    log.info("Account balance updated for accountNum: {}", accountNum);
                } else {
                    log.info("No balance change for accountNum: {}", accountNum);
                }
                accountVO.setFetchedDate(LocalDateTime.now());
                accountMapper.updateAccount(accountVO);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd' 'HH:mm:ss");
                accountResponseDto.setFetchedDate(LocalDateTime.now().format(formatter));
                return accountResponseDto;
            }
        }
        throw new Exception("Account not found.");
    }

    @Override
    public void addTransaction(Long userId, TransactionRequestDto transactionRequestDto) throws Exception {

        //1. 요청 dto에 connectedId 추가
        UserVO userVO = userMapper.findById(userId);
        String connectedId = userVO.getConnectedId();
        log.info("user info {}" , connectedId);

        String accountNum = transactionRequestDto.getAccount();
        transactionRequestDto.setConnectedId(connectedId);
        transactionRequestDto.setOrderBy("0");
        log.info(transactionRequestDto.toString());

        //1-1. 가장 최근 거래 시간 가져와서 startDate 설정
        LocalDateTime latestTrTime = accountMapper.findLatestTransactionDateByAccount(accountNum);
        if(latestTrTime != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            String startDate = latestTrTime.toLocalDate().format(formatter);
            transactionRequestDto.setStartDate(startDate);
        }

        //2. 거래내역 조회
        CodefTransactionResponseDto codefTransactionResponseDto = codefApiRequester.getTransactionList(transactionRequestDto);
        if (codefTransactionResponseDto == null) {
            log.info("None transaction list added.");
            return;
        }

        List<CodefTransactionResponseDto.HistoryItem> trItemList = codefTransactionResponseDto.getResTrHistoryList();
        List<TransactionVO> transactionVOList = mapToTransactionList(accountNum, trItemList);

        //3. 기존거래와 중복 저장 방지
        List<TransactionVO> existingTransactions = accountMapper.findAllTransactionsVo(accountNum);

        Set<String> existingTransactionKeys = existingTransactions.stream()
                .map(tx -> tx.getTrTime() + "_" + tx.getTrAccountIn() + "_" + tx.getTrAccountOut())
                .collect(Collectors.toSet());

        List<TransactionVO> transactionsToSave = transactionVOList.stream()
                .filter(tx -> !existingTransactionKeys.contains(tx.getTrTime() + "_" + tx.getTrAccountIn() + "_" + tx.getTrAccountOut()))
                .toList();

        // 4. 저장
        if (!transactionsToSave.isEmpty()) {
            accountMapper.insertTransaction(transactionsToSave);
            log.info("new transaction added: {}건", transactionsToSave.size());
        } else {
            log.info("no new transaction to add");
        }

    }

    private AccountVO mapToAccountVO(Long userId, AccountResponseDto accountResponseDto) {
        AccountVO accountVO = new AccountVO();
        accountVO.setUserId(userId);
        accountVO.setAccountNum(accountResponseDto.getAccountNum());
        accountVO.setBankId(accountResponseDto.getBankId());
        accountVO.setAccountName(accountResponseDto.getAccountName());
        accountVO.setAccountBalance(accountResponseDto.getAccountBalance());
        accountVO.setFetchedDate(LocalDateTime.now());
        return accountVO;
    }

    private List<TransactionVO> mapToTransactionList(String accountNum, List<CodefTransactionResponseDto.HistoryItem> historyItemList) throws Exception {
        List<TransactionVO> transactionVoList = new ArrayList<>();

        for (CodefTransactionResponseDto.HistoryItem item : historyItemList) {
            TransactionVO transactionVo = new TransactionVO();
            transactionVo.setAccountNum(accountNum);

            // 출금, 입금, 잔액 파싱 (빈 문자열이면 0 처리)
            int transactionIn = Integer.parseInt(item.getResAccountIn());
            int transactionOut = Integer.parseInt(item.getResAccountOut());
            int transactionBalance = Integer.parseInt(item.getResAfterTranBalance());
            transactionVo.setTrAccountIn(transactionIn);
            transactionVo.setTrAccountOut(transactionOut);
            transactionVo.setTrAfterBalance(transactionBalance);

            // 거래일자 및 시간 파싱
            String dateTimeStr = item.getResAccountTrDate() + " " + item.getResAccountTrTime(); // yyyyMMdd HHmmss
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HHmmss");
            LocalDateTime transactionDate = LocalDateTime.parse(dateTimeStr, formatter);
            transactionVo.setTrTime(transactionDate);

            // 출금 여부에 따라 거래 유형 설정
            transactionVo.setTransactionType(transactionOut != 0 ? "Output" : "Input");

            // 기타 설명 필드 매핑
            transactionVo.setTrDesc1(item.getResAccountDesc1());
            transactionVo.setTrDesc2(item.getResAccountDesc2());
            transactionVo.setTrDesc3(item.getResAccountDesc3());
            transactionVo.setTrDesc4(item.getResAccountDesc4());

            transactionVoList.add(transactionVo);
        }
        return transactionVoList;
    }


}
