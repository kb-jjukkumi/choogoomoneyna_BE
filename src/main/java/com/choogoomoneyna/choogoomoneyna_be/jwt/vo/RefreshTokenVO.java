package com.choogoomoneyna.choogoomoneyna_be.jwt.vo;

import lombok.Data;

import java.util.Date;

@Data
public class RefreshTokenVO {
    private Long id;                // PK
    private Long userId;            // 사용자 ID (FK)
    private String refreshToken;    // 실제 토큰 문자열
    private Date issuedAt;          // 발급 시각
    private Date expiresAt;         // 만료 시각
}