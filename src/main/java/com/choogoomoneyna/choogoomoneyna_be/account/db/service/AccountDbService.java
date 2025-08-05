package com.choogoomoneyna.choogoomoneyna_be.account.db.service;

import com.choogoomoneyna.choogoomoneyna_be.account.codef.dto.AccountResponseDto;
import com.choogoomoneyna.choogoomoneyna_be.account.codef.dto.TransactionRequestDto;
import com.choogoomoneyna.choogoomoneyna_be.account.codef.dto.TransactionResponseDto;
import com.choogoomoneyna.choogoomoneyna_be.account.db.dto.TransactionItemDto;

import java.util.List;

public interface AccountDbService {

    /**
     * DB에 등록돤 사용자의 계좌를 모두 조회합니다.
     * @param userId 사용자 아이디
     * @return 유저의 계좌 목록 반환, 없을 경우는 null
     */
    List<AccountResponseDto> getAllAccounts(Long userId);

    /**
     * account(계좌번호)로 등록된 거래내역을 모두 조회합니다.
     * @param account 계좌번호
     * @return 해당 계좌번호로 등록된 모든 거래내역
     */
    List<TransactionItemDto> getAllTransactions(String account);

    /**
     *  account(계좌번호)의 이번주 거래내역을 조회합니다.
     * @param account 계좌번호
     * @return 거래내역아이템 리스트
     */
    List<TransactionItemDto> getWeeklyTransactions(String account,String start, String end);

    /**
     * 해당 계좌가 어느 은행의 계좌인지, bankId를 조회합니다.
     * @param account 계좌번호
     * @return 은행아이디(bankId)
     */
    String getBankId(String account);
}
