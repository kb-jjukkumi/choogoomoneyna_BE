package com.choogoomoneyna.choogoomoneyna_be.auth.oauth.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OAuthUserInfoResponseDTO {
    private String oAuthEmail;
    private String nickname;
}
