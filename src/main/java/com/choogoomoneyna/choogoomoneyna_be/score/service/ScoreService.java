package com.choogoomoneyna.choogoomoneyna_be.score.service;

import com.choogoomoneyna.choogoomoneyna_be.score.vo.UserScoreVO;

import java.util.List;

public interface ScoreService {

    void createScore(UserScoreVO userScoreVO);

    int getScore(Long userId);

    void updateScore(UserScoreVO userScoreVO);

    List<UserScoreVO> getAllScores();

    List<UserScoreVO> getTopNScores(int limit);

    void deleteScoreByUserId(Long userId);
}
