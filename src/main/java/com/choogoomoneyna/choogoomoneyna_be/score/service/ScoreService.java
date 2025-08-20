package com.choogoomoneyna.choogoomoneyna_be.score.service;

import com.choogoomoneyna.choogoomoneyna_be.score.vo.UserScoreVO;

import java.util.List;

public interface ScoreService {

    /**
     * 사용자의 새로운 점수 항목을 생성합니다.
     *
     * @param userScoreVO 사용자 ID, 라운드 번호, 점수 값 및 추가 메타데이터를 포함하는
     *                    사용자 점수 객체
     */
    void createScore(UserScoreVO userScoreVO);

    /**
     * 제공된 사용자 점수 객체 목록을 사용하여 여러 점수 항목을 일괄 생성합니다.
     *
     * @param userScoreVOList 여러 사용자의 점수 세부 정보(사용자 ID, 라운드 번호, 점수 값 및
     *                        추가 메타데이터)가 포함된 UserScoreVO 객체 목록
     */
    void batchCreateScores(List<UserScoreVO> userScoreVOList);

    /**
     * 특정 라운드에 대한 사용자의 점수를 조회합니다.
     *
     * @param userId 점수를 조회할 사용자의 고유 식별자
     * @param roundNumber 점수를 조회할 라운드 번호
     * @return 지정된 라운드에 대한 사용자의 점수
     */
    int getScoreByUserIdAndRoundNumber(Long userId, Integer roundNumber);

    /**
     * 제공된 세부 정보로 사용자의 점수 항목을 업데이트합니다.
     * 이 작업은 제공된 사용자 점수 객체를 기반으로 기존 점수 데이터를 수정합니다.
     *
     * @param userScoreVO 사용자 ID, 라운드 번호 및 새로운 점수 값을 포함하는
     *                    업데이트된 점수 세부 정보가 담긴 사용자 점수 객체
     */
    void updateScore(UserScoreVO userScoreVO);

    /**
     * 지정된 라운드 번호에 대한 모든 사용자의 점수 목록을 조회합니다.
     *
     * @param roundNumber 모든 사용자의 점수를 조회할 라운드 번호
     * @return 지정된 라운드에 대한 모든 사용자의 점수 세부 정보가 포함된 UserScoreVO 객체 목록
     */
    List<UserScoreVO> findCurrentAllScores(int roundNumber);

    /**
     * 특정 라운드 번호에 대한 상위 N개의 사용자 점수를 조회합니다.
     * 이 메서드는 사용자 점수 목록을 가져와 점수 값을 기준으로 내림차순으로 정렬하여
     * 상위 N개의 결과만 반환합니다.
     *
     * @param roundNumber 상위 사용자 점수를 조회할 라운드 번호
     * @param limit 반환할 상위 점수의 최대 개수
     * @return 지정된 라운드의 상위 N개 점수를 나타내는 UserScoreVO 객체 목록
     */
    List<UserScoreVO> findTopNCurrentScoresByRoundNumber(Integer roundNumber, int limit);

    /**
     * 주어진 사용자 ID로 식별된 사용자의 점수 기록을 삭제합니다.
     *
     * @param userId 점수 기록을 삭제할 사용자의 고유 식별자
     */
    void deleteScoreByUserId(Long userId);

    /**
     * 특정 사용자의 레벨업 상태를 업데이트합니다.
     * 이 메서드는 사용자 ID로 지정된 사용자의 레벨업 플래그를 설정합니다.
     *
     * @param userId 레벨업 상태를 업데이트할 사용자의 고유 식별자
     * @param isLevelUp 사용자에게 할당할 새로운 레벨업 상태
     */
    void updateIsLevelUpByUserId(Long userId, boolean isLevelUp);
}
