package com.choogoomoneyna.choogoomoneyna_be.ranking.service;

import com.choogoomoneyna.choogoomoneyna_be.matching.service.RoundInfoService;
import com.choogoomoneyna.choogoomoneyna_be.ranking.vo.RankingUpdateVO;
import com.choogoomoneyna.choogoomoneyna_be.ranking.vo.RankingVO;
import com.choogoomoneyna.choogoomoneyna_be.score.service.ScoreService;
import com.choogoomoneyna.choogoomoneyna_be.score.vo.UserScoreVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RankingUpdateServiceImpl implements RankingUpdateService {

    private final RoundInfoService roundInfoService;
    private final RankingService rankingService;
    private final ScoreService scoreService;

    @Transactional
    @Override
    public void updateRanking() {
        int roundNumber = roundInfoService.getRoundNumber();

        List<UserScoreVO> sortedUserScores = new ArrayList<>(scoreService.findCurrentAllScores(roundNumber));
        sortedUserScores.sort(Comparator.comparingInt(UserScoreVO::getScoreValue).reversed());

        List<RankingUpdateVO> updateRankingList = new ArrayList<>();
        int befScore = Integer.MAX_VALUE;
        int rank = 0;

        for (int idx = 0; idx < sortedUserScores.size(); idx++) {
            UserScoreVO userScore = sortedUserScores.get(idx);
            if (befScore > userScore.getScoreValue()) {
                rank = idx + 1;
                befScore = userScore.getScoreValue();
            }

            RankingUpdateVO vo = RankingUpdateVO.builder()
                    .roundNumber(roundNumber)
                    .userId(userScore.getUserId())
                    .currentRanking(rank)
                    .updateDate(new Date()) // 현재 시간으로 설정
                    .build();

            updateRankingList.add(vo);
        }

        if (!updateRankingList.isEmpty()) {
            rankingService.batchUpdateCurrentRanking(updateRankingList);
        }
    }

    @Override
    public void createNewWeekRankings() {
        
//        // TODO: 느려지면 rankingMapper 에서 최근 것만 가져오도록 수정할 것
//        List<RankingVO> rankingList = rankingService.getAllRanking();
//        Map<Long, RankingVO> lastRankingMap = new HashMap<>();
//
//        for (RankingVO ranking : rankingList) {
//            lastRankingMap.merge(ranking.getUserId(), ranking, (existing, incoming) ->
//                    existing.getUpdateDate().after(incoming.getUpdateDate()) ? existing : incoming
//            );
//        }
//
//        List<RankingVO> lastRankingList = new ArrayList<>(lastRankingMap.values());
//        lastRankingList.sort(Comparator.comparingInt(RankingVO::getCurrentRanking).reversed());

        List<RankingVO> lastRankingList = rankingService.findLatestRankingPerUser();
        rankingService.batchInsertRankings(lastRankingList);
    }
}
