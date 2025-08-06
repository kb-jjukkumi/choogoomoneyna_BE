package com.choogoomoneyna.choogoomoneyna_be.account.db.service;

import com.choogoomoneyna.choogoomoneyna_be.account.codef.dto.AccountResponseDto;
import com.choogoomoneyna.choogoomoneyna_be.account.codef.vo.TransactionVO;
import com.choogoomoneyna.choogoomoneyna_be.account.db.dto.TransactionItemDto;
import com.choogoomoneyna.choogoomoneyna_be.account.db.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class AccountDbServiceImpl implements AccountDbService {

    private final AccountMapper accountMapper;

    @Override
    public List<AccountResponseDto> getAllAccounts(Long userId) {
        return accountMapper.findByUserId(userId);
    }

    @Override
    public List<TransactionItemDto> getAllTransactions(String account) {
        List<TransactionVO> voList = accountMapper.findAllTransactionsVo(account);

        return voList.stream().map(vo -> {
            TransactionItemDto dto = TransactionConverter.voToItemDto(vo);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<TransactionItemDto> getMonthlyTransactions(String account, String startDate, String endDate) {
        List<TransactionVO> voList = accountMapper.findTransactionsByAccountNumAndDateRange(account, startDate, endDate);

        return voList.stream()
                .map(TransactionConverter::voToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionItemDto> getWeeklyTransactions(String account, String start, String end) {

        List<TransactionVO> voList = accountMapper.findTransactionsByAccountNumAndDateRange(account, start, end);

        return voList.stream()
                .map(TransactionConverter::voToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionItemDto> getDailyTransactions(String account, String start, String end) {
        List<TransactionVO> voList = accountMapper.findDailyTransactions(account, start, end);

        return voList.stream()
                .map(TransactionConverter::voToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public String getBankId(String account) {
        return accountMapper.findBankIdByAccountNum(account);
    }
}
