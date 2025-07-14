package com.choogoomoneyna.choogoomoneyna_be.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserJoinDTO {
    private String email;
    private String password;
    private String nickname;
    private MultipartFile profileImage;
    private ChoogooMi choogooMi;
}
