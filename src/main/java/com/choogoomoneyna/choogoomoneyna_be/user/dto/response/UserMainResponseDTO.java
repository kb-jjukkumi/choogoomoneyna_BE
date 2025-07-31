package com.choogoomoneyna.choogoomoneyna_be.user.dto.response;

import com.choogoomoneyna.choogoomoneyna_be.user.enums.ChoogooMi;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserMainResponseDTO {
    private ChoogooMi choogooMi;
    private String nickname;
    private Integer userScore;
    private String userRanking;
    private Boolean isLevelUp;
}
