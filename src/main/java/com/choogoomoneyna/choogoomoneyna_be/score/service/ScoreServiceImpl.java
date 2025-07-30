package com.choogoomoneyna.choogoomoneyna_be.score.service;

import com.choogoomoneyna.choogoomoneyna_be.score.mapper.ScoreMapper;
import com.choogoomoneyna.choogoomoneyna_be.score.vo.UserScoreVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScoreServiceImpl implements ScoreService {

    private final ScoreMapper scoreMapper;

    @Override
    public void createScore(UserScoreVO userScoreVO) {
        scoreMapper.insertScore(userScoreVO);
    }

    @Override
    public int getScoreByUserIdAndRoundNumber(Long userId, Integer roundNumber) {
        return scoreMapper.getScoreByUserIdAndRoundNumber(userId, roundNumber);
    }

    @Override
    public void updateScore(UserScoreVO userScoreVO) {
        scoreMapper.updateScore(userScoreVO);
    }

    @Override
    public List<UserScoreVO> findCurrentAllScores(int roundNumber) {
        return scoreMapper.findCurrentAllScores(roundNumber);
    }

    @Override
    public List<UserScoreVO> findTopNCurrentScoresByRoundNumber(Integer roundNumber, int limit) {
        return scoreMapper.findTopNCurrentScoresByRoundNumber(roundNumber, limit);
    }

    @Override
    public void deleteScoreByUserId(Long userId) {
        scoreMapper.deleteByUserId(userId);
    }
}
