package com.choogoomoneyna.choogoomoneyna_be.ranking.vo;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RankingVO {
    long userId;
    Integer previousRanking;
    Integer currentRanking;
}
