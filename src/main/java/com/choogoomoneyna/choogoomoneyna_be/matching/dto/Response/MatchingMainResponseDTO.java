package com.choogoomoneyna.choogoomoneyna_be.matching.dto.Response;

import com.choogoomoneyna.choogoomoneyna_be.mission.dto.response.MissionProgressDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class MatchingMainResponseDTO {
    private String message;
    private List<MissionProgressDTO> myMissionProgressList;
    private List<MissionProgressDTO> opponentMissionProgressList;
}
