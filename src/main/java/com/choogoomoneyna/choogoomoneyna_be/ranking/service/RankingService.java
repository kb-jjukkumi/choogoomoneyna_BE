package com.choogoomoneyna.choogoomoneyna_be.ranking.service;

import com.choogoomoneyna.choogoomoneyna_be.ranking.vo.RankingVO;

public interface RankingService {

    void createRanking(RankingVO rankingVO);

    RankingVO findRankingByUserId(Long userId);

    Integer getCurrentRanking(Long userId);
}
