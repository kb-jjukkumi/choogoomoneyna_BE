package com.choogoomoneyna.choogoomoneyna_be.ranking.dto.response;

import com.choogoomoneyna.choogoomoneyna_be.user.enums.ChoogooMi;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RankingChangeResponseDTO {
    private String userNickname;
    private Integer ranking;
    private Integer beforeRanking;
    private Integer score;
    private ChoogooMi choogooMi;
}
