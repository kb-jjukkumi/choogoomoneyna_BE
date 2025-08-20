package com.choogoomoneyna.choogoomoneyna_be.matching.service;

import com.choogoomoneyna.choogoomoneyna_be.matching.dto.Response.MatchingMainResponseDTO;

public interface MatchingDetailService {

    /**
     * 특정 라운드에서 사용자의 상세 매칭 정보를 조회합니다.
     *
     * @param userId      매칭 정보를 조회할 사용자의 ID
     * @param roundNumber 매칭 정보를 조회할 라운드 번호
     * @return 사용자의 매칭 상세 정보를 포함하는 MatchingMainResponseDTO 객체를 반환합니다.
     * 여기에는 미션 진행 상황, 총 점수, 사용자와 상대방의 순위 정보가 포함됩니다.
     */
    MatchingMainResponseDTO getMatchingDetail(Long userId, Integer roundNumber);

}
