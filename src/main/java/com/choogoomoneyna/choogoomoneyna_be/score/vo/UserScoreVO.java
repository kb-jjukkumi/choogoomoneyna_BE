package com.choogoomoneyna.choogoomoneyna_be.score.vo;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserScoreVO {
    private Integer roundNumber;  // 복합키 1
    private Long userId;          // 복합키 2
    private Integer scoreValue;
    private Date regDate;
    private Date updateDate;
    private Boolean isLevelUp;
}
