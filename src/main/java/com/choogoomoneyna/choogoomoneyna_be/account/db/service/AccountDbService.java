package com.choogoomoneyna.choogoomoneyna_be.account.db.service;

import com.choogoomoneyna.choogoomoneyna_be.account.codef.dto.AccountResponseDto;

import java.util.List;

public interface AccountDbService {

    List<AccountResponseDto> getAllAccounts(Long userId);
}
