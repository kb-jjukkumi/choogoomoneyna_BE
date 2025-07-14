package com.choogoomoneyna.choogoomoneyna_be.user.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserVO {
    private Long id;
    private String email;
    private String password;
    private String nickname;
    private String profileImageUrl;  // DB에는 URL 저장
    private Date regDate;
    private Date updateDate;
    private String choogooMi;        // Enum → String
    private Integer score1;
    private Integer score2;
    private Integer score3;
    private Integer score4;
    private Integer score5;
}

