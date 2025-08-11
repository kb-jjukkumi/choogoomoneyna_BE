package com.choogoomoneyna.choogoomoneyna_be.matching.service.opneAi;

import com.choogoomoneyna.choogoomoneyna_be.matching.dto.Response.TextAiResponseDTO;

public interface MissionAiService {

    TextAiResponseDTO validateMission3(String mission, String answer);
}
