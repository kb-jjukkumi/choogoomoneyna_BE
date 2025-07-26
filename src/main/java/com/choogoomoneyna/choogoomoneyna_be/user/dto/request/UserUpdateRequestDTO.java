package com.choogoomoneyna.choogoomoneyna_be.user.dto.request;

import com.choogoomoneyna.choogoomoneyna_be.user.enums.ChoogooMi;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdateRequestDTO {
    private String nickname;
    private MultipartFile profileImage;
    private ChoogooMi choogooMi;
}
