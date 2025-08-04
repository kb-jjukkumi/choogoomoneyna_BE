package com.choogoomoneyna.choogoomoneyna_be.mission.vo;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MissionVO {
    private Integer id;
    private String ChoogooMiName;
    private String missionTitle;
    private String missionContent;
    private Integer missionScore;
    private String missionType;
    private Integer missionLimitAmount;
}
