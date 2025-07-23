package com.choogoomoneyna.choogoomoneyna_be.account.codef.service;

import com.choogoomoneyna.choogoomoneyna_be.account.codef.dto.AccountRequestDto;
import com.choogoomoneyna.choogoomoneyna_be.account.codef.dto.AccountResponseDto;
import com.choogoomoneyna.choogoomoneyna_be.account.codef.dto.AccountUpdateRequestDto;

import java.util.List;

public interface CodefService {

    List<AccountResponseDto> addAccount(Long userId, AccountRequestDto accountRequestDto) throws Exception;

    AccountResponseDto updateAccountOne(Long userId, AccountUpdateRequestDto accountUpdateRequestDto) throws Exception;
}
