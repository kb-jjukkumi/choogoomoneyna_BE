package com.choogoomoneyna.choogoomoneyna_be.ranking.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
@Builder
public class RankingHistoryDTO {
    private Date regDate;
    private Integer ranking;
    private Integer score;
}
