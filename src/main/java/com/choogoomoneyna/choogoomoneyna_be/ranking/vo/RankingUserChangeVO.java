package com.choogoomoneyna.choogoomoneyna_be.ranking.vo;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RankingUserChangeVO {
    private Integer roundNumber;
    private Long userId;
    private String nickname;
    private Integer ranking;
    private Integer beforeRanking;
    private Integer score;
    private String choogooMi;
}
