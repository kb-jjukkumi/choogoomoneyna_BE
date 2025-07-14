package com.choogoomoneyna.choogoomoneyna_be.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private String email;
    private String password;
    private String nickname;
    private Date regDate;  // 등록일
    private Date updateDate;  // 수정일
    private MultipartFile profileImage;
    private ChoogooMi choogooMi;  // 추구미 유형
}