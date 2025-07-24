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

}
