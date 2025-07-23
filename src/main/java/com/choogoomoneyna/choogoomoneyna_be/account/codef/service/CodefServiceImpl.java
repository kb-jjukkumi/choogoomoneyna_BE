package com.choogoomoneyna.choogoomoneyna_be.account.codef.service;

import com.choogoomoneyna.choogoomoneyna_be.account.codef.dto.*;
import com.choogoomoneyna.choogoomoneyna_be.account.codef.vo.AccountVO;
import com.choogoomoneyna.choogoomoneyna_be.account.codef.vo.TransactionVO;
import com.choogoomoneyna.choogoomoneyna_be.account.db.mapper.AccountMapper;
import com.choogoomoneyna.choogoomoneyna_be.user.mapper.UserMapper;
import com.choogoomoneyna.choogoomoneyna_be.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
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

    private final SqlSessionTemplate sqlSessionTemplate;
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
            log.info("None account added.");
            throw new Exception("check id/password.");
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

        // 2. codef api로 계좌 리스트 가져오기
        List<AccountResponseDto> accountList = codefApiRequester.getAccountOne(bankId, connectedId);

        if (accountList == null || accountList.isEmpty()) {
            System.out.println("accountList is empty."+ accountList);
            log.info("None account added.");
            throw new Exception("check id/password.");
        }

        //3. 동일 계좌 찾기
        for(AccountResponseDto accountResponseDto : accountList) {
            if (accountResponseDto.getAccountNum().equals(accountNum)) {
                AccountVO accountVO = accountMapper.findByAccountNum(accountNum);
                if (accountVO == null) {
                    throw new Exception("Account not found in DB.");
                }

                //5. 잔액 비교 후 다르면 업데이트
                if(!accountVO.getAccountBalance().equals(accountResponseDto.getAccountBalance())) {
                    accountVO.setAccountBalance(accountResponseDto.getAccountBalance());
                    accountVO.setUpdateDate(LocalDateTime.now());
                    accountMapper.updateAccount(accountVO);
                    log.info("Account balance updated for accountNum: {}", accountNum);
                } else {
                    log.info("No balance change for accountNum: {}", accountNum);
                }
                return accountResponseDto;
            }
        }
        throw new Exception("Account not found.");
    }

    @Override
    public TransactionResponseDto addTransaction(Long userId, TransactionRequestDto transactionRequestDto) throws Exception {

        //1. 요청 dto에 connectedId 추가
        UserVO userVO = userMapper.findById(userId);
        String connectedId = userVO.getConnectedId();
        log.info("user info {}" , connectedId);
        transactionRequestDto.setConnectedId(connectedId);
        transactionRequestDto.setOrderBy("0");
        log.info(transactionRequestDto.toString());

        //2. 거래내역 조회
         CodefTransactionResponseDto codefTransactionResponseDto = codefApiRequester.getTransactionList(transactionRequestDto);
        if (codefTransactionResponseDto == null) {
            log.info("None transaction list added.");
        }

        String accountNum = transactionRequestDto.getAccount();

        List<CodefTransactionResponseDto.HistoryItem> trItemList = codefTransactionResponseDto.getResTrHistoryList();
        List<TransactionVO> transactionVOList = mapToTransactionList(accountNum, trItemList);

        // 3. 최근 이틀치 거래 내역 조회 (최적화된 쿼리 수행)
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(2);
        List<TransactionVO> existingTransactions = accountMapper.findTransactionsByAccountNumAndDateRange(
                accountNum, startDate, endDate);

        if (existingTransactions.isEmpty()) {
            // 기존 거래가 없으면 모든 거래를 저장
            accountMapper.insertTransaction(transactionVOList);
            log.info("모든 거래 내역이 저장되었습니다.");
        }

        // 4. 기존 거래 내역을 HashSet에 저장 (중복 체크용 키 생성)
        Set<String> existingTransactionKeys = existingTransactions.stream()
                .map(tx -> tx.getTrTime() + "_" + tx.getTrAccountIn() + "_" + tx.getTrAccountOut())
                .collect(Collectors.toSet());

        // 5. 중복되지 않는 거래만 필터링
        List<TransactionVO> transactionsToSave = transactionVOList.stream()
                .filter(tx -> !existingTransactionKeys.contains(tx.getTrTime() + "_" + tx.getTrAccountIn() + "_" + tx.getTrAccountOut()))
                .toList();

        // 6. 새로운 거래 내역이 있다면 저장
        if (!transactionsToSave.isEmpty()) {
            accountMapper.insertTransaction(transactionsToSave);
            log.info("new transaction added");
        } else {
            log.info("no new transaction to add");
        }

        // 4. TransactionVO → TransactionResponseDto 변환
        List<TransactionResponseDto.trItem> dtoList = transactionVOList.stream()
                .map(tx -> {
                    TransactionResponseDto.trItem dto = new TransactionResponseDto.trItem();
                    dto.setTransactionId(tx.getTransactionId());
                    dto.setTrDate(tx.getTrTime().toLocalDate().toString());  // yyyy-MM-dd
                    dto.setTrTime(Integer.parseInt(tx.getTrTime().toLocalTime().format(DateTimeFormatter.ofPattern("HHmmss")))); // HHmmss
                    dto.setTrAccountOut(String.valueOf(tx.getTrAccountOut()));
                    dto.setTrAccountIn(String.valueOf(tx.getTrAccountIn()));
                    dto.setTrAfterBalance(String.valueOf(tx.getTrAfterBalance()));
                    dto.setTransactionType(tx.getTransactionType());
                    dto.setTrDesc1(tx.getTrDesc1());
                    dto.setTrDesc2(tx.getTrDesc2());
                    dto.setTrDesc3(tx.getTrDesc3());
                    dto.setTrDesc4(tx.getTrDesc4());
                    return dto;
                })
                .toList();

        TransactionResponseDto responseDto = new TransactionResponseDto();
        responseDto.setAccountNum(accountNum);
        responseDto.setTransactionList(dtoList);
        return responseDto;
        //return accountMapper.findTransactionsByAccountNumAndDateRange(accountNum, startDate, endDate);

    }
    private AccountVO mapToAccountVO(Long userId, AccountResponseDto accountResponseDto) {
        AccountVO accountVO = new AccountVO();
        accountVO.setUserId(userId);
        accountVO.setAccountNum(accountResponseDto.getAccountNum());
        accountVO.setBankId(accountResponseDto.getBankId());
        accountVO.setAccountName(accountResponseDto.getAccountName());
        accountVO.setAccountBalance(accountResponseDto.getAccountBalance());
        return accountVO;
    }

//    private List<TransactionVO> mapToTransactionList(String accountNum, List<TransactionResponseDto.trItem> trItemList) throws Exception {
//        List<TransactionVO> transactionVoList = new ArrayList<>();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HHmmss");
//
//        for (TransactionResponseDto.trItem trItem : trItemList) {
//            TransactionVO transactionVo = new TransactionVO();
//            transactionVo.setAccountNum(accountNum);
//
//            int transactionIn = Integer.parseInt(trItem.getTrAccountIn());
//            int transactionOut = Integer.parseInt(trItem.getTrAccountOut());
//            int transactionBalance = Integer.parseInt(trItem.getTrAfterBalance());
//            transactionVo.setTrAccountIn(transactionIn);
//            transactionVo.setTrAccountOut(transactionOut);
//            transactionVo.setTrAfterBalance(transactionBalance);
//
//            LocalDateTime transactionDate = LocalDateTime.parse(trItem.getTrDate() + " " + trItem.getTrTime());
//            transactionVo.setTrTime(transactionDate);
//            transactionVo.setTransactionType(transactionOut != 0 ? "출금" : "입금");
//
//            transactionVo.setTrDesc1(trItem.getTrDesc1());
//            transactionVo.setTrDesc2(trItem.getTrDesc2());
//            transactionVo.setTrDesc3(trItem.getTrDesc3());
//            transactionVo.setTrDesc4(trItem.getTrDesc4());
//
//            transactionVoList.add(transactionVo);
//        }
//        return transactionVoList;
//    }

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
