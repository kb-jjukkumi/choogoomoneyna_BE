package com.choogoomoneyna.choogoomoneyna_be.auth.jwt.service;

import com.choogoomoneyna.choogoomoneyna_be.config.JwtConfig;
import com.choogoomoneyna.choogoomoneyna_be.auth.jwt.util.JwtTokenProvider;
import com.choogoomoneyna.choogoomoneyna_be.auth.jwt.mapper.RefreshTokenMapper;
import com.choogoomoneyna.choogoomoneyna_be.auth.jwt.vo.RefreshTokenVO;
import com.choogoomoneyna.choogoomoneyna_be.exception.CustomException;
import com.choogoomoneyna.choogoomoneyna_be.exception.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenMapper refreshTokenMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtConfig jwtConfig;

    @Override
    public String generateRefreshTokenAndSave(Long userId) {
        try {
            String refreshToken = jwtTokenProvider.generateRefreshToken(userId);

            Date issuedAt = new Date(); // 현재 시간
            Date expiresAt = new Date(issuedAt.getTime() + jwtConfig.getRefreshTokenExpirationTime());

            refreshTokenMapper.insertRefreshToken(userId, refreshToken, issuedAt, expiresAt);

            return refreshToken;
        } catch (Exception e) {
            throw new CustomException(
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "RefreshToken 생성 중 알 수 없는 오류가 발생했습니다.",
                    e
            );
        }
    }

    @Override
    public void deleteAllTokensByUserId(Long userId) {
        try {
            refreshTokenMapper.deleteByUserId(userId);
        } catch (Exception e) {
            throw new CustomException(
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "전체 RefreshToken 삭제 중 오류가 발생했습니다.",
                    e
            );
        }
    }

    @Override
    public void deleteTokenByRefreshToken(String refreshToken) {
        try {
            refreshTokenMapper.deleteByRefreshToken(refreshToken);
        } catch (Exception e) {
            throw new CustomException(
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "RefreshToken 삭제 중 오류가 발생했습니다.",
                    e
            );
        }
    }

    @Override
    public boolean isRefreshTokenValid(String refreshToken) {
        try {
            RefreshTokenVO tokenVO = refreshTokenMapper.findByRefreshToken(refreshToken);
            return tokenVO != null && tokenVO.getExpiresAt().after(new Date());
        } catch (Exception e) {
            throw new CustomException(
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "RefreshToken 검증 중 오류가 발생했습니다.",
                    e
            );
        }
    }

    @Override
    public String reissueAccessToken(String refreshToken) {
        try {
            if (!isRefreshTokenValid(refreshToken)) {
                throw new RuntimeException("유효하지 않은 RefreshToken입니다.");
            }

            RefreshTokenVO tokenVO = refreshTokenMapper.findByRefreshToken(refreshToken);

            // 예: User 닉네임은 userService로 조회 필요 (간단히 userId만 넣음)
            return jwtTokenProvider.generateAccessToken(tokenVO.getUserId());
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException(
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "AccessToken 재발급 처리 중 알 수 없는 오류가 발생했습니다.",
                    e
            );
        }
    }
}
