package com.choogoomoneyna.choogoomoneyna_be.matching.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class MatchingMissionResultVO {
    private Long id;
    private Long matchingId;
    private Integer missionId;
    private Long userId;
    private Integer resultScore;
}
