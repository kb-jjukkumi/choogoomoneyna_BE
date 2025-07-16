package com.choogoomoneyna.choogoomoneyna_be.user.controller;

import com.choogoomoneyna.choogoomoneyna_be.jwt.JwtTokenProvider;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @ModelAttribute UserJoinRequestDTO dto) {
        userService.registerUser(dto);
        return ResponseEntity.ok("회원가입 성공!");
    }

    @PostMapping("/login")
    public JwtTokenResponseDTO login(@Valid @RequestBody UserLoginRequestDTO dto) throws IllegalAccessException {
        UserVO user = userService.findByEmailAndLoginType(dto.getEmail(), LoginType.LOCAL);

        if (user == null) {
            throw new UsernameNotFoundException("존재하지 않은 이메일입니다.");
        }

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtTokenProvider.generateAccessToken(user.getId(), user.getNickname());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId(), user.getNickname());

        return new JwtTokenResponseDTO(accessToken, refreshToken);
    }
}
