package com.choogoomoneyna.choogoomoneyna_be.auth.jwt.mapper;

import com.choogoomoneyna.choogoomoneyna_be.auth.jwt.vo.RefreshTokenVO;
import org.apache.ibatis.annotations.Param;
import org.mapstruct.Mapper;

import java.util.Date;

@Mapper
public interface RefreshTokenMapper {
    void insertRefreshToken(@Param("userId") Long userId,
                            @Param("refreshToken") String refreshToken,
                            @Param("issuedAt") Date issuedAt,
                            @Param("expiresAt") Date expiresAt);

    void deleteByUserId(@Param("userId") Long userId);

    void deleteByRefreshToken(@Param("refreshToken") String refreshToken);

    RefreshTokenVO findByRefreshToken(@Param("refreshToken") String refreshToken);
}

