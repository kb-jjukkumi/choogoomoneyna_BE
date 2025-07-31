package com.choogoomoneyna.choogoomoneyna_be.account.codef.service;

import com.choogoomoneyna.choogoomoneyna_be.account.codef.dto.*;
import com.choogoomoneyna.choogoomoneyna_be.account.codef.vo.TransactionVO;

import java.util.List;

public interface CodefService {

    List<AccountResponseDto> addAccount(Long userId, AccountRequestDto accountRequestDto, String mockScenario) throws Exception;

    AccountResponseDto updateAccountOne(Long userId, AccountUpdateRequestDto accountUpdateRequestDto) throws Exception;

    void addTransaction(Long userId, TransactionRequestDto transactionRequestDto) throws Exception;
}
