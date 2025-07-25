package com.choogoomoneyna.choogoomoneyna_be.ranking.service;

import com.choogoomoneyna.choogoomoneyna_be.ranking.vo.RankingUpdateVO;
import com.choogoomoneyna.choogoomoneyna_be.ranking.vo.RankingVO;

import java.util.List;

public interface RankingService {

    void createRanking(RankingVO rankingVO);

    void updateCurrentRankingByUserId(Long userId, int currentRanking);

    RankingVO findRankingByUserId(Long userId);

    Integer getCurrentRanking(Long userId);

    void rolloverWeeklyRankings();

    void batchUpdateCurrentRanks(List<RankingUpdateVO> rankingList);

    void updateRanking();
}
