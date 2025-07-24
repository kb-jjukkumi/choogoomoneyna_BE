package com.choogoomoneyna.choogoomoneyna_be.auth.jwt;

import com.choogoomoneyna.choogoomoneyna_be.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final UserMapper userMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtProperties jwtProperties;

    public String generateRefreshTokenAndSave(Long userId, String nickname) {
        String refreshToken = jwtTokenProvider.generateRefreshToken(userId);

        // TODO: DB에 저장할거면 추가 할 것
        // Date expirationTime = new Date(System.currentTimeMillis() + jwtProperties.getRefreshTokenExpirationTime());

        return refreshToken;
    }
}
