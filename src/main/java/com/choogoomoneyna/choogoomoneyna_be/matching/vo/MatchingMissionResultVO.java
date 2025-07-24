package com.choogoomoneyna.choogoomoneyna_be.matching.vo;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchingMissionResultVO {
    private Long id;
    private Long matchingId;
    private Integer missionId;
    private Long userId;
    private Integer resultScore;
}
