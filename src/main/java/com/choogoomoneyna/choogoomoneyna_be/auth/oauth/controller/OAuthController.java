package com.choogoomoneyna.choogoomoneyna_be.auth.oauth.controller;

import com.choogoomoneyna.choogoomoneyna_be.auth.jwt.CustomUserDetails;
import com.choogoomoneyna.choogoomoneyna_be.auth.jwt.JwtTokenProvider;
import com.choogoomoneyna.choogoomoneyna_be.auth.jwt.service.RefreshTokenService;
import com.choogoomoneyna.choogoomoneyna_be.auth.oauth.dto.OAuthUserInfoDTO;
import com.choogoomoneyna.choogoomoneyna_be.auth.oauth.service.KakaoLoginService;
import com.choogoomoneyna.choogoomoneyna_be.exception.InvalidTokenException;
import com.choogoomoneyna.choogoomoneyna_be.user.dto.request.JwtTokenResponseDTO;
import com.choogoomoneyna.choogoomoneyna_be.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/oauth2")
public class OAuthController {

    private final KakaoLoginService kakaoLoginService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    @GetMapping("/kakao/login")
    public ResponseEntity<?> kakaoLogin(@RequestParam String code) {
        try {
            String kakaoAccessToken = kakaoLoginService.getAccessToken(code);
            OAuthUserInfoDTO userInfo = kakaoLoginService.getUserInfo(kakaoAccessToken);
            
            // db 에 없으면 저장
            UserVO user = kakaoLoginService.findOrCreateUserByOAuth(userInfo);

            // 기존 토큰 삭제
            refreshTokenService.deleteAllTokensByUserId(user.getId());
            
            String accessToken = jwtTokenProvider.generateAccessToken(user.getId());
            String refreshToken = refreshTokenService.generateRefreshTokenAndSave(user.getId());
            log.info("accessToken = {}", accessToken);
            log.info("refreshToken = {}", refreshToken);

            JwtTokenResponseDTO responseDTO = new JwtTokenResponseDTO(accessToken, refreshToken);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/kakao/logout")
    public ResponseEntity<?> kakaoLogout(@RequestHeader(value = "refreshToken") String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new InvalidTokenException("refreshToken 헤더가 없습니다.");
        }

        refreshTokenService.deleteTokenByRefreshToken(refreshToken);
        SecurityContextHolder.clearContext();

        return ResponseEntity.ok("로그아웃 성공");
    }

    @PostMapping("/kakao/withdraw")
    public ResponseEntity<?> kakaoWithdraw(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestHeader("kakaoAccessToken") String kakaoAccessToken
    ) {
        kakaoLoginService.unlinkKakao(kakaoAccessToken);
        
        // TODO: Users 테이블에도 삭제 할 것

        refreshTokenService.deleteAllTokensByUserId(userDetails.getId());

        return ResponseEntity.ok("카카오 회원 탈퇴 완료!");
    }
}
