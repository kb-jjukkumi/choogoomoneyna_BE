package com.choogoomoneyna.choogoomoneyna_be.account.db.service;

import com.choogoomoneyna.choogoomoneyna_be.account.codef.dto.AccountResponseDto;

import java.util.List;

public interface AccountDbService {

    /**
     * DB에 등록돤 사용자의 계좌를 모두 조회합니다.
     * @param userId 사용자 아이디
     * @return 유저의 계좌 목록 반환, 없을 경우는 null
     */
    List<AccountResponseDto> getAllAccounts(Long userId);
}
