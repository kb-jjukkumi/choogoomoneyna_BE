package com.choogoomoneyna.choogoomoneyna_be.user.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class UserPasswordResetDTO {
    @NotBlank(message = "이메일은 필수입니다")
    private String email;
    
    @NotBlank(message = "인증되지 않은 유저")
    private String verificationCode;
    
    @NotBlank(message = "패스워드는 필수입니다")
    private String newPassword;
}

