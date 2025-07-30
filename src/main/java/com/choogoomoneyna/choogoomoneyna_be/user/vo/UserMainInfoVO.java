package com.choogoomoneyna.choogoomoneyna_be.user.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMainInfoVO {
    private Long userId;
    private String choogooMi;
    private String nickname;
    private Integer userScore;
    private String userRanking;
    private Boolean isLevelUp;
}
