package com.choogoomoneyna.choogoomoneyna_be.ranking.vo;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RankingVO {
    Integer roundNumber;  // 복합키 1
    Long userId;       // 복합키 2
    Integer currentRanking;
    Date regDate;
    Date updateDate;
}
