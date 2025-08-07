package com.choogoomoneyna.choogoomoneyna_be.auth.oauth.dto;

import com.choogoomoneyna.choogoomoneyna_be.user.enums.LoginType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OAuthUserInfoDTO {
    private String oAuthEmail;
    private String nickname;
}
