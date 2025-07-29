package com.choogoomoneyna.choogoomoneyna_be.ranking.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RankingUserVO {
    private Long userId;
    private String nickname;
    private Integer ranking;
    private Integer score;
}
