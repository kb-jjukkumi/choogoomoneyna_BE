package com.choogoomoneyna.choogoomoneyna_be.score.service;

import com.choogoomoneyna.choogoomoneyna_be.score.vo.UserScoreVO;

import java.util.List;

public interface ScoreService {

    void createScore(UserScoreVO userScoreVO);

    int getScoreByUserIdAndRoundNumber(Long userId, Integer roundNumber);

    void updateScore(UserScoreVO userScoreVO);

    List<UserScoreVO> findCurrentAllScores();

    List<UserScoreVO> findTopNCurrentScoresByRoundNumber(Integer roundNumber, int limit);

    void deleteScoreByUserId(Long userId);
}
