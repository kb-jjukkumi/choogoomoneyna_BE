package com.choogoomoneyna.choogoomoneyna_be.account.codef.service;

import com.choogoomoneyna.choogoomoneyna_be.account.codef.dto.*;
import com.choogoomoneyna.choogoomoneyna_be.account.codef.vo.TransactionVO;

import java.util.List;

public interface CodefService {

    List<AccountResponseDto> addAccount(Long userId, AccountRequestDto accountRequestDto);

    AccountResponseDto updateAccountOne(Long userId, AccountUpdateRequestDto accountUpdateRequestDto);

    void addTransaction(Long userId, TransactionRequestDto transactionRequestDto);
}
