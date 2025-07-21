package com.choogoomoneyna.choogoomoneyna_be.auth.email.controller;

import com.choogoomoneyna.choogoomoneyna_be.auth.email.dto.EmailAuthRequestDTO;
import com.choogoomoneyna.choogoomoneyna_be.auth.email.dto.EmailVerifyRequestDTO;
import com.choogoomoneyna.choogoomoneyna_be.auth.email.service.EmailAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email-auth")
@RequiredArgsConstructor
public class EmailAuthController {

    private final EmailAuthService emailAuthService;

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
}
