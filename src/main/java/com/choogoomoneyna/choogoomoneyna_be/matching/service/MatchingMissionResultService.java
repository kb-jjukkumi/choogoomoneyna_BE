package com.choogoomoneyna.choogoomoneyna_be.matching.service;

public interface MatchingMissionResultService {

    /**
     * 매칭된 미션의 결과 하나를 초기 저장합니다
     *
     * @param userId 사용자 ID
     * @param matchingId 매칭 ID
     * @param missionId 미션 ID
     */
    void createMatchingMissionResult(Long userId, Long matchingId, Integer missionId);

    /**
     * 매칭된 미션의 결과 점수를 업데이트합니다.
     *
     * @param userId       사용자 ID
     * @param matchingId   매칭 ID
     * @param missionId    미션 ID
     * @param missionScore 미션 점수
     */
    void updateMatchingMissionResult(Long userId, Long matchingId, Integer missionId, Integer missionScore);

    /**
     * 모든 매칭된 미션의 결과를 업데이트합니다.
     */
    void updateAllResults();

    /**
     * 이번 매칭 회차에 특정 유저가 얻은 최종 점수를 반환
     *
     * @param userId 사용자 ID
     * @param matchingId 매칭 ID
     */
    int getAllScoreByUserIdAndMatchingId(Long userId, Long matchingId);

}
