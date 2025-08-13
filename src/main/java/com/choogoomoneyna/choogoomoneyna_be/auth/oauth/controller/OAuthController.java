package com.choogoomoneyna.choogoomoneyna_be.auth.oauth.controller;

import com.choogoomoneyna.choogoomoneyna_be.auth.jwt.util.CustomUserDetails;
import com.choogoomoneyna.choogoomoneyna_be.auth.jwt.service.RefreshTokenService;
import com.choogoomoneyna.choogoomoneyna_be.auth.oauth.dto.request.OAuthRequestDTO;
import com.choogoomoneyna.choogoomoneyna_be.auth.oauth.service.KakaoLoginService;
import com.choogoomoneyna.choogoomoneyna_be.exception.InvalidTokenException;
import com.choogoomoneyna.choogoomoneyna_be.user.dto.request.JwtTokenResponseDTO;
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
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/kakao/login")
    public ResponseEntity<?> kakaoLogin(@RequestBody OAuthRequestDTO request) {
        String code = request.getCode();

        System.out.println(code);
        try {
            JwtTokenResponseDTO response = kakaoLoginService.login(code);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(405).body(e);
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
