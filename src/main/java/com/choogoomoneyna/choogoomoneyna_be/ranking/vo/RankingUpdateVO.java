package com.choogoomoneyna.choogoomoneyna_be.ranking.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RankingUpdateVO {
    private Long userId;
    private Integer currentRanking;
}
