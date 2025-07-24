package com.choogoomoneyna.choogoomoneyna_be.score.vo;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserScoreVO {
    private Long userId;
    private Integer score;
}
