package com.choogoomoneyna.choogoomoneyna_be.mission.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class MissionProgressListResponseDTO {
    private String message;
    private List<MissionProgressDTO> missionProgressDTOList;
    private int resultScore;
}
