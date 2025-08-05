package com.choogoomoneyna.choogoomoneyna_be.mission.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MissionProgressDTO {
    int missionId;
    String missionTitle;
    String missionType;
    String missionContent;
    Integer missionScore;
    String userNickname;
    int score;
}
