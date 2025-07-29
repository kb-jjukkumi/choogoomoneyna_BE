package com.choogoomoneyna.choogoomoneyna_be.ranking.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RankingResponseDTO {
    private String userNickname;
    private Integer ranking;
    private Integer score;
}
