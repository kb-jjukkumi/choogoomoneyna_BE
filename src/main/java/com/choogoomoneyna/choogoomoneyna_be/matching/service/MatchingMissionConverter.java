package com.choogoomoneyna.choogoomoneyna_be.matching.service;

import com.choogoomoneyna.choogoomoneyna_be.mission.dto.response.MissionProgressDTO;
import com.choogoomoneyna.choogoomoneyna_be.mission.vo.UserMissionVO;

public class MatchingMissionConverter {

    /**
     * UserMissionVO -> MissionProgressDTO 변환
     */
    public static MissionProgressDTO toMissionProgressDTO(UserMissionVO vo) {
        return MissionProgressDTO.builder()
                .missionId(vo.getMissionId())
                .userNickname(vo.getNickname())
                .missionTitle(vo.getMissionTitle())
                .missionType(vo.getMissionType())
                .missionContent(vo.getMissionContent())
                .missionScore(vo.getMissionScore())
                .score(vo.getResultScore() != null ? vo.getResultScore() : 0)
                .build();
    }
}
