package com.choogoomoneyna.choogoomoneyna_be.ranking.vo;

import com.choogoomoneyna.choogoomoneyna_be.user.enums.ChoogooMi;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RankingHistoryVO {
    String ChoogooMi;
    Integer roundNumber;
    Long userId;
    Integer ranking;
    Integer score;
    Date regDate;
}
