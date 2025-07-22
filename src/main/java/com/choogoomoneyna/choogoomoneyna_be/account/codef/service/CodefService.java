package com.choogoomoneyna.choogoomoneyna_be.account.codef.service;

import com.choogoomoneyna.choogoomoneyna_be.account.codef.dto.AccountRequestDto;
import com.choogoomoneyna.choogoomoneyna_be.account.codef.dto.AccountResponseDto;

import java.util.List;

public interface CodefService {

    List<AccountResponseDto> addAccount(Long userId, AccountRequestDto accountRequestDto) throws Exception;

    AccountResponseDto updateAccountOne(Long userId, String bankId, String accountNum) throws Exception;
}
