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
    private int id;
    private int matchingId;
    private int missionId;
    private long userId;
    private int resultScore;
}
