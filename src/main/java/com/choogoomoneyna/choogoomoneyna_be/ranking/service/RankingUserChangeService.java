package com.choogoomoneyna.choogoomoneyna_be.ranking.service;

import com.choogoomoneyna.choogoomoneyna_be.ranking.vo.RankingUserChangeVO;

import java.util.List;

public interface RankingUserChangeService {

    List<RankingUserChangeVO> findTopNRankingUserChangeByRoundNumber(int roundNumber, int limit);
}
