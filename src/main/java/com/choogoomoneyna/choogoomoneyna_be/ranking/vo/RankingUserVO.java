package com.choogoomoneyna.choogoomoneyna_be.ranking.vo;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RankingUserVO {
    private Integer roundNumber;
    private Long userId;
    private String nickname;
    private Integer ranking;
    private Integer score;
    private String ChoogooMi;
}
