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
    public int getScore(Long userId) {
        return scoreMapper.getScore(userId);
    }

    @Override
    public void updateScore(UserScoreVO userScoreVO) {
        scoreMapper.updateScore(userScoreVO);
    }

    @Override
    public List<UserScoreVO> getAllScores() {
        return scoreMapper.findAllScores();
    }

    @Override
    public List<UserScoreVO> getTopNScores(int limit) {
        return scoreMapper.findTopNScores(limit);
    }

    @Override
    public void deleteScoreByUserId(Long userId) {
        scoreMapper.deleteByUserId(userId);
    }
}
