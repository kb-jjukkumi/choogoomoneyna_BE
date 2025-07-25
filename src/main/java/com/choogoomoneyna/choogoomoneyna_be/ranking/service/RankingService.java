package com.choogoomoneyna.choogoomoneyna_be.ranking.service;

import com.choogoomoneyna.choogoomoneyna_be.ranking.vo.RankingVO;

public interface RankingService {

    void createRanking(RankingVO rankingVO);

    void updateCurrentRankingByUserId(Long userId, int currentRank);

    RankingVO findRankingByUserId(Long userId);

    Integer getCurrentRanking(Long userId);

    void rolloverWeeklyRankings();
}
