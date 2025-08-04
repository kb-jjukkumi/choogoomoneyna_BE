package com.choogoomoneyna.choogoomoneyna_be.matching.service;

import com.choogoomoneyna.choogoomoneyna_be.exception.CustomNotFoundException;
import com.choogoomoneyna.choogoomoneyna_be.matching.dto.Response.MatchingMainResponseDTO;
import com.choogoomoneyna.choogoomoneyna_be.mission.dto.response.MissionProgressDTO;
import com.choogoomoneyna.choogoomoneyna_be.ranking.service.RankingService;
import com.choogoomoneyna.choogoomoneyna_be.score.service.ScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchingDetailServiceImpl implements MatchingDetailService {

    private final MatchingService matchingService;
    private final MatchingMissionResultService matchingMissionResultService;
    private final ScoreService scoreService;
    private final RankingService rankingService;

    @Override
    public MatchingMainResponseDTO getMatchingDetail(Long userId, Integer roundNumber) {
        Long matchId = matchingService.getProgressMatchingIdByUserIdAndRoundNumber(userId, roundNumber);
        if (matchId == null) {
            throw new CustomNotFoundException("matching progress not found");
        }

        Long opponentUserId = matchingService.getComponentUserIdByUserIdAndMatchingId(userId, matchId);
        if (opponentUserId == null) {
            throw new CustomNotFoundException("opponent user not found");
        }

        List<MissionProgressDTO> myProgressList = matchingMissionResultService.getAllMissionProgressDTO(userId, matchId);
        List<MissionProgressDTO> opponentProgressList = matchingMissionResultService.getAllMissionProgressDTO(opponentUserId, matchId);
        Integer myScore = scoreService.getScoreByUserIdAndRoundNumber(userId, roundNumber);
        Integer opponentScore = scoreService.getScoreByUserIdAndRoundNumber(opponentUserId, roundNumber);
        Integer myRanking = rankingService.findCurrentRankingByUserId(userId);
        Integer opponentRanking = rankingService.findCurrentRankingByUserId(opponentUserId);

        return MatchingMainResponseDTO.builder()
                .message("Matching main detail")
                .myMissionProgressList(myProgressList)
                .opponentMissionProgressList(opponentProgressList)
                .myTotalScore(myScore)
                .opponentTotalScore(opponentScore)
                .myRanking(myRanking)
                .opponentRanking(opponentRanking)
                .build();
    }

}
