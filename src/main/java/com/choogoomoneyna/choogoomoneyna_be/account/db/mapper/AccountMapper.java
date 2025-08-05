package com.choogoomoneyna.choogoomoneyna_be.account.db.mapper;

import com.choogoomoneyna.choogoomoneyna_be.account.codef.dto.AccountResponseDto;
import com.choogoomoneyna.choogoomoneyna_be.account.codef.dto.TransactionResponseDto;
import com.choogoomoneyna.choogoomoneyna_be.account.codef.vo.AccountVO;
import com.choogoomoneyna.choogoomoneyna_be.account.codef.vo.TransactionVO;
import com.choogoomoneyna.choogoomoneyna_be.account.db.dto.TransactionItemDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface AccountMapper {

    AccountVO findByAccountNum(String accountNum);

    void insertAccount(List<AccountVO> accountVOList);

    void updateAccount(AccountVO accountVO);

    List<TransactionVO> findTransactionsByAccountNumAndDateRange(
            @Param("accountNum") String accountNum,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate);

    List<TransactionVO> findDailyTransactions(@Param("accountNum") String accountNum,
                                              @Param("startDate") String startDate,
                                              @Param("endDate") String endDate);

    void insertTransaction(List<TransactionVO> transactionVO);

    List<AccountResponseDto> findByUserId(Long userId);

    List<TransactionVO> findAllTransactionsVo(String account);

    LocalDateTime findLatestTransactionDateByAccount(String accountNum);

    String findBankIdByAccountNum(String accountNum);

}
