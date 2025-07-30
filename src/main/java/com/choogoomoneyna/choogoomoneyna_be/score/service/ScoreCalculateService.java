package com.choogoomoneyna.choogoomoneyna_be.score.service;

public interface ScoreCalculateService {

    /**
     * 특정 사용자의 점수를 업데이트하고 레벨업 여부를 판단합니다.
     * 사용자의 점수를 업데이트하고 필요한 경우 레벨업 로직을 실행합니다.
     *
     * @param userId          점수를 계산할 사용자의 ID
     * @param roundNumber     점수를 업데이트할 현재 라운드 번호
     * @param calculatedScore 사용자의 현재 점수에 더할 새로운 점수
     */
    void calculateScore(Long userId, Integer roundNumber, Integer calculatedScore);
}