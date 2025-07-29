package com.choogoomoneyna.choogoomoneyna_be.ranking.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RankingUpdateVO {
    private Long userId;
    private Integer currentRanking;
    private Date updateDate;
}
