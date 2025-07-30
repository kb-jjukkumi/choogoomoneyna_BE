package com.choogoomoneyna.choogoomoneyna_be.user.mapper;

import com.choogoomoneyna.choogoomoneyna_be.user.vo.UserMainInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMainInfoMapper {

    UserMainInfoVO findUserMainResponseByUserIdAndRoundNumber(@Param("userId") Long userId, @Param("roundNumber") Integer roundNumber);
}
