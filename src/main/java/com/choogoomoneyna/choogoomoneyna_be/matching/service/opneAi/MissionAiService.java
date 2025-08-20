package com.choogoomoneyna.choogoomoneyna_be.matching.service.opneAi;

import com.choogoomoneyna.choogoomoneyna_be.matching.dto.Response.TextAiResponseDTO;

public interface MissionAiService {

    /**
     * AI를 사용하여 특정 미션에 대한 사용자의 답변을 검증하고 점수를 계산합니다.
     *
     * @param mission 검증할 미션 또는 과제의 내용
     * @param answer 미션에 대한 사용자의 답변
     * @return 사용자의 답변에 대한 AI 생성 점수가 포함된 TextAiResponseDTO
     */
    TextAiResponseDTO validateMission3(String mission, String answer);
}
