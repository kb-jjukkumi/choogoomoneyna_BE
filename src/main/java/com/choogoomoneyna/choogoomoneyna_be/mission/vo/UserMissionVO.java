package com.choogoomoneyna.choogoomoneyna_be.mission.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserMissionVO {
    private Long userId;
    private Integer missionId;
    private String missionTitle;
    private String missionContent;
    private Integer missionScore;
    private Integer ResultScore;
}
