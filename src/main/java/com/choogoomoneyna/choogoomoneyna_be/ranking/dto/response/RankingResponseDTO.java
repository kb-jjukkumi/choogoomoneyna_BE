package com.choogoomoneyna.choogoomoneyna_be.ranking.dto.response;

import com.choogoomoneyna.choogoomoneyna_be.user.enums.ChoogooMi;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RankingResponseDTO {
    private String userNickname;
    private Integer ranking;
    private Integer score;
    private ChoogooMi ChoogooMi;
}
