package com.choogoomoneyna.choogoomoneyna_be.score.service;

import com.choogoomoneyna.choogoomoneyna_be.score.vo.UserScoreVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScoreCalculateServiceImpl implements ScoreCalculateService {

    private static final int[] levelUpScores = {0, 300, 800, 1500};

    private final ScoreService scoreService;

    @Override
    public void calculateScore(Long userId, Integer roundNumber, Integer calculatedScore) {
        int beforeScore = scoreService.getScoreByUserIdAndRoundNumber(userId, roundNumber);
        int updateScore = beforeScore + calculatedScore;
        log.info("🔄 점수 업데이트 - userId: {}, round: {}, before: {}, after: {}", userId, roundNumber, beforeScore, updateScore);

        scoreService.updateScore(
                UserScoreVO.builder()
                        .userId(userId)
                        .roundNumber(roundNumber)
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
