package com.choogoomoneyna.choogoomoneyna_be.account.codef.mapper;

import com.choogoomoneyna.choogoomoneyna_be.account.codef.vo.AccountVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AccountMapper {

    AccountVO findAccountByAccountNum(String accountNum);
}
