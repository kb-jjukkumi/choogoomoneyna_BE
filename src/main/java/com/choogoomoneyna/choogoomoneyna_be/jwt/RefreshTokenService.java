package com.choogoomoneyna.choogoomoneyna_be.jwt;

import com.choogoomoneyna.choogoomoneyna_be.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final UserMapper userMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtProperties jwtProperties;

    public String generateRefreshTokenAndSave(Long userId, String nickname) {
        String refreshToken = jwtTokenProvider.generateRefreshToken(userId, nickname);

        // TODO: DB에 저장할거면 추가 할 것
        // Date expirationTime = new Date(System.currentTimeMillis() + jwtProperties.getRefreshTokenExpirationTime());

        return refreshToken;
    }
}
