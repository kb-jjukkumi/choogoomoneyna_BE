package com.choogoomoneyna.choogoomoneyna_be.matching.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MatchingMissionUpdateRequest {

    @NotNull
    private Integer missionId;
}
