package com.choogoomoneyna.choogoomoneyna_be.ranking.service;

import com.choogoomoneyna.choogoomoneyna_be.ranking.mapper.RankingMapper;
import com.choogoomoneyna.choogoomoneyna_be.ranking.vo.RankingVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RankingServiceImpl implements RankingService {

    private RankingMapper rankingMapper;

    @Override
    public void createRanking(RankingVO rankingVO) {
        rankingMapper.insertRanking(rankingVO);
    }

    @Override
    public void updateCurrentRankingByUserId(Long userId, int currentRank) {
        rankingMapper.updateCurrentRankingByUserId(userId, currentRank);
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
}
