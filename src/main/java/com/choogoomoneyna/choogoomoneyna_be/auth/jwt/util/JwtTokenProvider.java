package com.choogoomoneyna.choogoomoneyna_be.auth.jwt.util;

import com.choogoomoneyna.choogoomoneyna_be.config.JwtConfig;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtConfig jwtConfig;

    /**
     * JWT 토큰 서명에 사용되는 비밀키(SecretKey)를 생성하여 반환
     * 이 비밀키는 JwtProperties에서 설정한 문자열(secretKey)을 UTF-8 바이트 배열로 변환한 후,
     * HMAC SHA 알고리즘에 맞게 SecretKey 객체로 생성함
     *
     * @return JWT 서명에 사용되는 SecretKey 객체
     */
    public SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtConfig.getSecretKey().getBytes(StandardCharsets.UTF_8));
    }

    /**
     * userId 와 nickname을 이용하여 JWT access token 생성
     *
     * @param userId   토큰을 생성할 사용자의 ID
     * @return 생성된 JWT access token String
     */
    public String generateAccessToken(Long userId) {
        Date now = new Date();  // 현재 시각
        Date expirationDate = new Date(now.getTime() + jwtConfig.getAccessTokenExpirationTime());  // 만료 시각

        return Jwts.builder()
                .claim("userId", userId)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    /**
     * userId를 이용하여 JWT refresh token 생성
     *
     * @param userId   토큰을 생성할 사용자의 ID
     * @return 생성된 JWT refresh token String
     */
    public String generateRefreshToken(Long userId) {
        Date now = new Date();  // 현재 시각
        Date expirationDate = new Date(now.getTime() + jwtConfig.getRefreshTokenExpirationTime());  // 만료 시각

        return Jwts.builder()
                .claim("userId", userId)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * JWT 토큰에서 사용자의 id를 추출
     *
     * @param token 파싱할 JWT 토큰
     * @return 토큰에서 추출한 사용자의 id
     */
    public Long getUserIdFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("userId", Long.class);
    }

    /**
     * 주어진 JWT 토큰의 유효성을 검사
     * 토큰이 유효한 형식이고 서명이 올바른 경우 true를 반환하고,
     * 그렇지 않은 경우(만료, 잘못된 서명, 잘못된 형식 등) false를 반환
     *
     * @param token 검증할 JWT 토큰
     * @return 토큰이 유효한 경우 true, 그렇지 않은 경우 false
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
