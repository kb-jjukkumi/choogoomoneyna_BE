package com.choogoomoneyna.choogoomoneyna_be.ranking.dto.response;

import com.choogoomoneyna.choogoomoneyna_be.user.enums.ChoogooMi;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class RankingHistoryDTO {
    ChoogooMi choogooMi;
    Integer roundNumber;
    private Date startDate;
    private Integer ranking;
    private Integer score;
}
