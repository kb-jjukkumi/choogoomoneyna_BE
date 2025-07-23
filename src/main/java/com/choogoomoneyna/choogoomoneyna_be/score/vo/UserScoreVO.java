package com.choogoomoneyna.choogoomoneyna_be.score.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserScoreVO {
    private Long userId;
    private Integer score;
}
