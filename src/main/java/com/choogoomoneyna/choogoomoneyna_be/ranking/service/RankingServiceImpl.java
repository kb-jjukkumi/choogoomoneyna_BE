package com.choogoomoneyna.choogoomoneyna_be.ranking.service;

import com.choogoomoneyna.choogoomoneyna_be.ranking.mapper.RankingMapper;
import com.choogoomoneyna.choogoomoneyna_be.ranking.vo.RankingUpdateVO;
import com.choogoomoneyna.choogoomoneyna_be.ranking.vo.RankingVO;
import com.choogoomoneyna.choogoomoneyna_be.score.service.ScoreService;
import com.choogoomoneyna.choogoomoneyna_be.score.vo.UserScoreVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RankingServiceImpl implements RankingService {

    private final RankingMapper rankingMapper;
    private final ScoreService scoreService;

    @Override
    public void createRanking(RankingVO rankingVO) {
        rankingMapper.insertRanking(rankingVO);
    }

    @Override
    public void updateCurrentRankingByUserId(Long userId, int currentRanking) {
        rankingMapper.updateCurrentRankingByUserId(userId, currentRanking);
    }

    @Override
    public RankingVO findRankingByUserId(Long userId) {
        return rankingMapper.findRankingByUserId(userId);
    }

    @Override
    public Integer getCurrentRanking(Long userId) {
        return rankingMapper.getCurrentRanking(userId);
    }

    @Override
    public void rolloverWeeklyRankings() {
        rankingMapper.rolloverWeeklyRankings();
    }

    @Override
    public void batchUpdateCurrentRanks(List<RankingUpdateVO> rankingList) {
        rankingMapper.batchUpdateCurrentRanks(rankingList);
    }

    @Transactional
    public void updateRanking() {
        // 이번주 랭킹을 저번주 랭킹으로 이월
        rolloverWeeklyRankings();

        List<UserScoreVO> sortedUserScores = new ArrayList<>(scoreService.getAllScores());
        sortedUserScores.sort(Comparator.comparingInt(UserScoreVO::getScore).reversed());

        List<RankingUpdateVO> updateRankingList = new ArrayList<>();
        int befScore = Integer.MAX_VALUE;
        int rank = 0;
        for (int idx = 0; idx < sortedUserScores.size(); idx++) {
            UserScoreVO userScore = sortedUserScores.get(idx);
            if (befScore > userScore.getScore()) {
                rank = idx+1;
                befScore = userScore.getScore();
            }
//            rankingService.updateCurrentRankingByUserId(userScore.getUserId(), befRank);

            updateRankingList.add(RankingUpdateVO.builder()
                    .userId(userScore.getUserId())
                    .currentRanking(rank)
                    .build());

        }

        // 일괄 업데이트
        batchUpdateCurrentRanks(updateRankingList);
    }
}
