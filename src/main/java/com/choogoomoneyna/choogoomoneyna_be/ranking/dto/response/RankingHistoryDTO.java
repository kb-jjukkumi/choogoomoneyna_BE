package com.choogoomoneyna.choogoomoneyna_be.ranking.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class RankingHistoryDTO {
    private Date startDate;
    private Integer ranking;
    private Integer score;
}
