package com.choogoomoneyna.choogoomoneyna_be.score.service;

import com.choogoomoneyna.choogoomoneyna_be.score.vo.UserScoreVO;

import java.util.List;

public interface ScoreService {

    void createScore(UserScoreVO userScoreVO);

    void batchCreateScores(List<UserScoreVO> userScoreVOList);

    int getScoreByUserIdAndRoundNumber(Long userId, Integer roundNumber);

    void updateScore(UserScoreVO userScoreVO);

    List<UserScoreVO> findCurrentAllScores(int roundNumber);

    List<UserScoreVO> findTopNCurrentScoresByRoundNumber(Integer roundNumber, int limit);

    void deleteScoreByUserId(Long userId);

    void updateIsLevelUpByUserId(Long userId, boolean isLevelUp);
}
