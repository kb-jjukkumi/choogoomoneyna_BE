package com.choogoomoneyna.choogoomoneyna_be.account.db.mapper;

import com.choogoomoneyna.choogoomoneyna_be.account.codef.vo.AccountVO;
import com.choogoomoneyna.choogoomoneyna_be.account.codef.vo.TransactionVO;
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
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    void insertTransaction(List<TransactionVO> transactionVO);
}
