package com.choogoomoneyna.choogoomoneyna_be.ranking.vo;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RankingVO {
    long rankingId;
    long userId;
    Integer currentRanking;
    Date regDate;
    Date updateDate;
}
