package com.choogoomoneyna.choogoomoneyna_be.auth.jwt.service;

import com.choogoomoneyna.choogoomoneyna_be.auth.jwt.JwtProperties;
import com.choogoomoneyna.choogoomoneyna_be.auth.jwt.JwtTokenProvider;
import com.choogoomoneyna.choogoomoneyna_be.auth.jwt.mapper.RefreshTokenMapper;
import com.choogoomoneyna.choogoomoneyna_be.auth.jwt.vo.RefreshTokenVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenMapper refreshTokenMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtProperties jwtProperties;

    @Override
    public String generateRefreshTokenAndSave(Long userId, String nickname) {
        String refreshToken = jwtTokenProvider.generateRefreshToken(userId, nickname);

        Date issuedAt = new Date(); // 현재 시간
        Date expiresAt = new Date(issuedAt.getTime() + jwtProperties.getRefreshTokenExpirationTime());

        refreshTokenMapper.insertRefreshToken(userId, refreshToken, issuedAt, expiresAt);

        return refreshToken;
    }

    @Override
    public void deleteAllTokensByUserId(Long userId) {
        refreshTokenMapper.deleteByUserId(userId);
    }

    @Override
    public void deleteTokenByRefreshToken(String refreshToken) {
        refreshTokenMapper.deleteByRefreshToken(refreshToken);
    }

    @Override
    public boolean isRefreshTokenValid(String refreshToken) {
        RefreshTokenVO tokenVO = refreshTokenMapper.findByRefreshToken(refreshToken);
        if (tokenVO == null) {
            return false;
        }
        return tokenVO.getExpiresAt().after(new Date());
    }

    @Override
    public String reissueAccessToken(String refreshToken) {
        if (!isRefreshTokenValid(refreshToken)) {
            throw new RuntimeException("유효하지 않은 RefreshToken입니다.");
        }

        RefreshTokenVO tokenVO = refreshTokenMapper.findByRefreshToken(refreshToken);

        // 예: User 닉네임은 userService로 조회 필요 (간단히 userId만 넣음)
        return jwtTokenProvider.generateAccessToken(tokenVO.getUserId(), "닉네임은 필요에 따라 조회");
    }
}
