package com.choogoomoneyna.choogoomoneyna_be.mission.vo;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMissionVO {
    private Long userId;
    private String nickname;
    private Integer missionId;
    private String missionTitle;
    private String missionContent;
    private Integer missionScore;
    private Integer ResultScore;
}
