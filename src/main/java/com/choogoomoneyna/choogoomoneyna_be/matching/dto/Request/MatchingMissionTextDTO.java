package com.choogoomoneyna.choogoomoneyna_be.matching.dto.Request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MatchingMissionTextDTO {

    @NotNull
    private Integer missionId;

    @NotNull
    private String contents;
}
