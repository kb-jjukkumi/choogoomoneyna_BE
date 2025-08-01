package com.choogoomoneyna.choogoomoneyna_be.ranking.service;


import com.choogoomoneyna.choogoomoneyna_be.ranking.vo.RankingHistoryVO;

import java.util.List;

public interface RankingHistoryService {

    List<RankingHistoryVO> findAllRankingHistoryByUserId(Long userId);
}
