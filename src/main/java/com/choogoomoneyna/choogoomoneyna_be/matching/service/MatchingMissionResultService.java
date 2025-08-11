package com.choogoomoneyna.choogoomoneyna_be.matching.service;

import com.choogoomoneyna.choogoomoneyna_be.matching.vo.MatchingMissionResultVO;
import com.choogoomoneyna.choogoomoneyna_be.mission.dto.response.MissionProgressDTO;

import java.util.List;

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

    /**
     * 특정 사용자와 매칭에 대한 모든 미션의 진행 상황을 조회합니다.
     *
     * @param userId  미션 진행 상황을 조회할 사용자의 ID
     * @param matchId 미션 진행 상황과 연관된 매칭의 ID
     * @return 미션 진행 상황 정보가 담긴 {@code MatchingMissionResultVO} 객체 리스트
     */
    List<MatchingMissionResultVO> getMatchingMissionResults(Long userId, Long matchId);

    /**
     * 특정 사용자와 매칭에 대한 모든 미션의 진행 상황 조회하여
     * MissionProgressDTO 형태로 변환하여 반환합니다
     *
     * @param userId  미션 진행 상황을 조회할 사용자의 ID
     * @param matchId 미션 진행 상황과 연관된 매칭의 ID
     * @return 미션 진행 상황 정보가 담긴 {@code MissionProgressDTO} 객체 리스트
     */
    List<MissionProgressDTO> getAllMissionProgressDTO(Long userId, Long matchId);

    /**
     * 미션 타입 1번, CODEF_WEEKLY 미션 결과를 검증합니다.
     * @param userId 미션 검증을 진행할 사용자의 ID
     * @param matchingId 미션이 포함되어있는 매칭 ID
     * @param missionId 해당 미션 ID
     * @param missionScore 해당 미션의 만점 점수
     * @param limitAmount 해당 미션의 지출 제한 금액 값
     */
    void validateMissionType1(Long userId, Long matchingId, Integer missionId, Integer missionScore, Integer limitAmount);

    /**
     * 미션 타입 2번, CODEF_DAILY 미션 결과를 검증합니다.
     * @param userId 미션 검증을 진행할 사용자의 ID
     * @param matchingId 미션이 포함되어있는 매칭 ID
     * @param missionId 해당 미션 ID
     * @param missionScore 해당 미션의 만점 점수
     * @param limitAmount 해당 미션의 지출 제한 금액 값
     */
    void validateMissionType2(Long userId, Long matchingId, Integer missionId, Integer missionScore, Integer limitAmount);

    /**
     * 미션 타입 3번, 텍스트 인풋 미션의 결과를 검증합니다.
     * open ai의 내용 검증 결과 점수 100점 만점에 70점 이상이면 성공입니다.
     * @param userId 미션 검증을 진행할 사용자의 ID
     * @param matchingId 미션이 포함되어있는 매칭 ID
     * @param missionId 해당 미션 ID
     * @param contents 사용자가 작성한 글 내용
     * @param score 해당 미션의 만점 점수
     */
    void validateMissionType3(Long userId, Long matchingId, Integer missionId, String contents, Integer score);

    /**
     * 유저 아이디와 매칭 아이디로 미션 3개 아이디 리스트를 조회하는 메서드
     * @param userId 유저아이디
     * @param matchingId 매칭아이디
     * @return
     */
    List<Integer> findMissionIdsByUserIdAndMatchingId(Long userId, Long matchingId);

}
