package com.choogoomoneyna.choogoomoneyna_be.user.controller;

import com.choogoomoneyna.choogoomoneyna_be.jwt.JwtTokenProvider;
import com.choogoomoneyna.choogoomoneyna_be.jwt.service.RefreshTokenService;
import com.choogoomoneyna.choogoomoneyna_be.jwt.service.RefreshTokenServiceImpl;
import com.choogoomoneyna.choogoomoneyna_be.user.dto.LoginType;
import com.choogoomoneyna.choogoomoneyna_be.user.dto.request.JwtTokenResponseDTO;
import com.choogoomoneyna.choogoomoneyna_be.user.dto.request.UserJoinRequestDTO;
import com.choogoomoneyna.choogoomoneyna_be.user.dto.request.UserLoginRequestDTO;
import com.choogoomoneyna.choogoomoneyna_be.user.mapper.UserMapper;
import com.choogoomoneyna.choogoomoneyna_be.user.service.UserService;
import com.choogoomoneyna.choogoomoneyna_be.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @ModelAttribute UserJoinRequestDTO dto) {
        userService.registerUser(dto);
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

        String accessToken = jwtTokenProvider.generateAccessToken(user.getId(), user.getNickname());
        String refreshToken = refreshTokenService.generateRefreshTokenAndSave(user.getId(), user.getNickname());

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
}
