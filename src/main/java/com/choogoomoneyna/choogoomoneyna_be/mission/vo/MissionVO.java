package com.choogoomoneyna.choogoomoneyna_be.mission.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class MissionVO {
    private Integer id;
    private String ChoogooMiName;
    private String missionTitle;
    private String missionContent;
    private Integer missionScore;
    private String missionType;
}
