package com.choogoomoneyna.choogoomoneyna_be.account.db.mapper;

import com.choogoomoneyna.choogoomoneyna_be.account.codef.vo.AccountVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AccountMapper {

    AccountVO findByAccountNum(String accountNum);

    void insertAccount(List<AccountVO> accountVOList);

    void updateAccount(AccountVO accountVO);
}
