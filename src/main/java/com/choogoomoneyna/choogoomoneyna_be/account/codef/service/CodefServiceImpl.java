package com.choogoomoneyna.choogoomoneyna_be.account.codef.service;

import com.choogoomoneyna.choogoomoneyna_be.account.codef.dto.AccountRequestDto;
import com.choogoomoneyna.choogoomoneyna_be.account.codef.dto.AccountResponseDto;
import com.choogoomoneyna.choogoomoneyna_be.account.codef.vo.AccountVO;
import com.choogoomoneyna.choogoomoneyna_be.account.db.mapper.AccountMapper;
import com.choogoomoneyna.choogoomoneyna_be.user.mapper.UserMapper;
import com.choogoomoneyna.choogoomoneyna_be.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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

        //1. ���� ���� ����
        UserVO userVO = userMapper.findById(userId);
        String connectedId = userVO.getConnectedId();

        // 2. connectedId ó�� (���� ��� ��� api�� ����)
        if (connectedId == null) {
            connectedId = codefApiRequester.registerConnectedId(accountRequestDto);
            userMapper.updateConnectedId(userId, connectedId);
        } else {
            connectedId = codefApiRequester.addConnectedId(connectedId, accountRequestDto);
        }

        // 3. codef api�� ���� ����Ʈ ��������
        List<AccountResponseDto> accountList = codefApiRequester.getAccountList(accountRequestDto, connectedId);

        if (accountList == null || accountList.isEmpty()) {
            log.info("None account added.");
            throw new Exception("check id/password.");
        }

        // 5. ���� ������ AccountVo ����Ʈ�� ����
        List<AccountVO> accountVOList = accountList.stream()
                .map(accountInfo -> mapToAccountVO(userId, accountInfo))
                .toList();

        // 6. accounts db ����
        accountMapper.insertAccount(accountVOList);
        log.info("{} accounts added.", accountVOList.size());

        return accountList;
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

    @Override
    public AccountResponseDto updateAccountOne(Long userId,String bankId, String accountNum) throws Exception {
        //1. ���� ���� ����
        UserVO userVO = userMapper.findById(userId);
        String connectedId = userVO.getConnectedId();

        // 2. codef api�� ���� ����Ʈ ��������
        List<AccountResponseDto> accountList = codefApiRequester.getAccountOne(bankId, connectedId);

        if (accountList == null || accountList.isEmpty()) {
            log.info("None account added.");
            throw new Exception("check id/password.");
        }

        //3. ���� ���� ã��
        for(AccountResponseDto accountResponseDto : accountList) {
            if (accountResponseDto.getAccountNum().equals(accountNum)) {
                AccountVO accountVO = accountMapper.findByAccountNum(accountNum);
                if (accountVO == null) {
                    throw new Exception("Account not found in DB.");
                }

                //5. �ܾ� �� �� �ٸ��� ������Ʈ
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
}
