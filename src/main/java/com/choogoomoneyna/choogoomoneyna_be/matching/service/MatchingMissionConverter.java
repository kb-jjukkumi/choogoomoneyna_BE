package com.choogoomoneyna.choogoomoneyna_be.matching.service;

import com.choogoomoneyna.choogoomoneyna_be.mission.dto.response.MissionProgressDTO;
import com.choogoomoneyna.choogoomoneyna_be.mission.vo.UserMissionVO;

public class MatchingMissionConverter {

    /**
     * UserMissionVO -> MissionProgressDTO 변환
     */
    public static MissionProgressDTO toMissionProgressDTO(UserMissionVO vo) {
        if (vo == null) return null;

        return MissionProgressDTO.builder()
                .missionId(vo.getMissionId())
                .userId(vo.getUserId())
                .missionTitle(vo.getMissionTitle())
                .missionContent(vo.getMissionContent())
                .missionScore(vo.getMissionScore())
                .score(vo.getResultScore() != null ? vo.getResultScore() : 0)
                .build();
    }
}
