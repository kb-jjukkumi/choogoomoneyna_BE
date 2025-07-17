package com.choogoomoneyna.choogoomoneyna_be.user.dto.request;

import com.choogoomoneyna.choogoomoneyna_be.user.dto.ChoogooMi;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserJoinRequestDTO {

    @NotBlank(message = "이메일은 필수 입니다.")
    @Email(message = "올바른 이메일 형식이어야 합니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입니다.")
    private String password;

    @NotBlank(message = "닉네임은 필수로 설정해야 합니다")
    private String nickname;

    private MultipartFile profileImage;

    @NotNull(message = "추구미는 필수로 선택해야 합니다")
    private ChoogooMi choogooMi;
}
