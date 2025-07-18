package com.choogoomoneyna.choogoomoneyna_be.auth.email.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailVerifyRequestDTO {
    private String email;
    private String code;
}
