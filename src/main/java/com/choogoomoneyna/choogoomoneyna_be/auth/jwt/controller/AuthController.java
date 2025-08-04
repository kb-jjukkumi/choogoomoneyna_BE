package com.choogoomoneyna.choogoomoneyna_be.auth.jwt.controller;

import com.choogoomoneyna.choogoomoneyna_be.auth.email.service.EmailAuthService;
import com.choogoomoneyna.choogoomoneyna_be.auth.jwt.CustomUserDetails;
import com.choogoomoneyna.choogoomoneyna_be.auth.jwt.JwtTokenProvider;
import com.choogoomoneyna.choogoomoneyna_be.auth.jwt.service.AuthService;
import com.choogoomoneyna.choogoomoneyna_be.auth.jwt.service.RefreshTokenService;
import com.choogoomoneyna.choogoomoneyna_be.user.dto.request.UserPasswordResetDTO;
import com.choogoomoneyna.choogoomoneyna_be.user.enums.LoginType;
import com.choogoomoneyna.choogoomoneyna_be.user.dto.request.JwtTokenResponseDTO;
import com.choogoomoneyna.choogoomoneyna_be.user.dto.request.UserJoinRequestDTO;
import com.choogoomoneyna.choogoomoneyna_be.user.dto.request.UserLoginRequestDTO;
import com.choogoomoneyna.choogoomoneyna_be.user.service.UserService;
import com.choogoomoneyna.choogoomoneyna_be.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;
    private final EmailAuthService emailAuthService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @ModelAttribute UserJoinRequestDTO dto) {
        authService.registerUser(dto);
        return ResponseEntity.ok("회원가입 성공!");
    }

    @GetMapping("/check-nickname")
    public ResponseEntity<?> checkNickname(@RequestParam String nickname) {
        try {
            boolean isNicknameDuplicated = userService.isUserLoginIdDuplicated(nickname);
            System.out.println(isNicknameDuplicated ? "yes" : "no");
            return ResponseEntity.ok(isNicknameDuplicated);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("서버 오류: " + e.getMessage());
        }
    }

    @GetMapping("/check-nickname-for-update")
    public ResponseEntity<?> checkNicknameForUpdate(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam String nickname
    ) {
        Long userId = userDetails.getId();
        String currentNickname = userService.getNicknameByUserId(userId);

        boolean isNicknameDuplicated = !currentNickname.equals(nickname) && userService.isUserLoginIdDuplicated(nickname);
        return ResponseEntity.ok(isNicknameDuplicated);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginRequestDTO dto) throws IllegalAccessException {
        UserVO user = userService.findByEmailAndLoginType(dto.getEmail(), LoginType.LOCAL);

        if (user == null) {
            throw new UsernameNotFoundException("존재하지 않은 이메일입니다.");
        }

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            System.out.println("password diff");
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        // 중복 로그인 방지 위해 기존 토큰 삭제
        refreshTokenService.deleteAllTokensByUserId(user.getId());

        String accessToken = jwtTokenProvider.generateAccessToken(user.getId());
        String refreshToken = refreshTokenService.generateRefreshTokenAndSave(user.getId());

        System.out.println("accessToken = " + accessToken);
        System.out.println("refreshToken = " + refreshToken);

        JwtTokenResponseDTO responseDTO = new JwtTokenResponseDTO(accessToken, refreshToken);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String refreshToken = request.getHeader("refreshToken");
        if (refreshToken != null) {
            refreshTokenService.deleteTokenByRefreshToken(refreshToken);
        }

        SecurityContextHolder.clearContext();
        request.getSession().invalidate();

        return ResponseEntity.ok("로그아웃 성공");
    }

    @PutMapping("/password/reset")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody UserPasswordResetDTO dto) {
        // TODO: 인증 코드 저장한 경우 추가 인증하기
        // String verificationCode = dto.getVerificationCode();

        UserVO user = userService.findByEmailAndLoginType(dto.getEmail(), LoginType.LOCAL);
        if (user == null) {
            throw new UsernameNotFoundException("존재하지 않은 이메일입니다.");
        }

        userService.updatePasswordByUserEmail(dto.getEmail(), dto.getNewPassword());
        return ResponseEntity.ok("비밀번호 재설정 성공");
    }
}
