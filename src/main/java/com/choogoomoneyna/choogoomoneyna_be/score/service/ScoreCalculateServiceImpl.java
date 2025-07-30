package com.choogoomoneyna.choogoomoneyna_be.score.service;

import com.choogoomoneyna.choogoomoneyna_be.score.vo.UserScoreVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScoreCalculateServiceImpl implements ScoreCalculateService {

    private static final int[] levelUpScores = {0, 300, 800, 1500};

    private final ScoreService scoreService;

    @Override
    public void calculateScore(Long userId, Integer roundNumber, Integer calculatedScore) {
        int beforeScore = scoreService.getScoreByUserIdAndRoundNumber(userId, roundNumber);
        int updateScore = beforeScore + calculatedScore;
        scoreService.updateScore(
                UserScoreVO.builder()
                        .userId(userId)
                        .scoreValue(updateScore)
                        .build()
        );

        for (int levelUpScore : levelUpScores) {
            if (beforeScore < levelUpScore && levelUpScore <= updateScore) {
                scoreService.updateIsLevelUpByUserId(userId, true);
                break;
            }
        }
    }
}
