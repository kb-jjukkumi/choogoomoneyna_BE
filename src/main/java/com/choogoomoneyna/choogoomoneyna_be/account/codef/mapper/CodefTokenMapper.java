package com.choogoomoneyna.choogoomoneyna_be.account.codef.mapper;

import com.choogoomoneyna.choogoomoneyna_be.account.codef.vo.CodefTokenVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CodefTokenMapper {

    CodefTokenVO getLatestToken();

    void insertToken(CodefTokenVO token);

    void updateToken(CodefTokenVO token);
}
