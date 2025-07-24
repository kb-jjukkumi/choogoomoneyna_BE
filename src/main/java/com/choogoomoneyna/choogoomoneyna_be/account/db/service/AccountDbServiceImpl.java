package com.choogoomoneyna.choogoomoneyna_be.account.db.service;

import com.choogoomoneyna.choogoomoneyna_be.account.codef.dto.AccountResponseDto;
import com.choogoomoneyna.choogoomoneyna_be.account.db.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class AccountDbServiceImpl implements AccountDbService {

    private final AccountMapper accountMapper;

    @Override
    public List<AccountResponseDto> getAllAccounts(Long userId) {
        return accountMapper.findByUserId(userId);
    }
}
