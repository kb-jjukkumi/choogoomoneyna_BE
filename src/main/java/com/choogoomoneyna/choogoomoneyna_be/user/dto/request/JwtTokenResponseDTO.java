package com.choogoomoneyna.choogoomoneyna_be.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtTokenResponseDTO {
    private String accessToken;
    private String refreshToken;
}
