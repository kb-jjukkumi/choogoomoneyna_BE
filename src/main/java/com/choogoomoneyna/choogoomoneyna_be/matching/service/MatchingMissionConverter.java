package com.choogoomoneyna.choogoomoneyna_be.matching.service;

import com.choogoomoneyna.choogoomoneyna_be.matching.vo.MatchingMissionResultVO;
import com.choogoomoneyna.choogoomoneyna_be.mission.dto.response.MissionProgressDTO;

public class MatchingMissionConverter {

    /**
     * MatchingMissionResultVO -> MissionProgressDTO 변환
     */
    public static MissionProgressDTO toMissionProgressDTO(MatchingMissionResultVO vo) {
        if (vo == null) return null;

        return MissionProgressDTO.builder()
                .missionId(vo.getMissionId())
                .userId(vo.getUserId())
                .score(vo.getResultScore() != null ? vo.getResultScore() : 0)
                .build();
    }
}
