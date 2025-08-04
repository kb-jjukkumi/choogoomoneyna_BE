package com.choogoomoneyna.choogoomoneyna_be.auth.email.controller;

import com.choogoomoneyna.choogoomoneyna_be.auth.email.dto.EmailAuthRequestDTO;
import com.choogoomoneyna.choogoomoneyna_be.auth.email.dto.EmailVerifyRequestDTO;
import com.choogoomoneyna.choogoomoneyna_be.auth.email.service.EmailAuthService;
import com.choogoomoneyna.choogoomoneyna_be.user.enums.LoginType;
import com.choogoomoneyna.choogoomoneyna_be.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email-auth")
@RequiredArgsConstructor
public class EmailAuthController {

    private final EmailAuthService emailAuthService;
    private final UserService userService;

    @PostMapping("/send")
    public ResponseEntity<?> sendAuthCode(@RequestBody EmailAuthRequestDTO dto) {
        emailAuthService.sendAuthCode(dto.getEmail());
        return ResponseEntity.ok("인증 코드가 이메일로 전송되었습니다");
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyAuthCode(@RequestBody EmailVerifyRequestDTO dto) {
        boolean verified = emailAuthService.verifyAuthCode(dto.getEmail(), dto.getCode());
        return verified ?
                ResponseEntity.ok("이메일 인증 성공") :
                ResponseEntity.badRequest().body("이메일 인증 실패");
    }

    @PostMapping("/password-reset/send")
    public ResponseEntity<String> checkEmail(@RequestBody String email) {
        if (userService.existsByEmailAndLoginType(email, LoginType.LOCAL)) {
            emailAuthService.sendAuthCode(email);
        }
        
        // 보안 상 존재하지 않는 이메일을 위한 메시지는 사용하지 않음
        return ResponseEntity.ok("인증 코드가 이메일로 전송되었습니다");
    }
}
