package com.choogoomoneyna.choogoomoneyna_be.ranking.service;


import com.choogoomoneyna.choogoomoneyna_be.ranking.vo.RankingHistoryVO;

import java.util.List;

public interface RankingHistoryService {

    /**
     * 특정 사용자의 모든 랭킹 기록을 조회합니다.
     *
     * @param userId 랭킹 기록을 조회할 사용자의 고유 식별자 
     * @return 해당 사용자의 랭킹 기록이 포함된 {@link RankingHistoryVO} 객체 리스트,
     *         랭킹 기록이 없는 경우 빈 리스트 반환 
     */
    List<RankingHistoryVO> findAllRankingHistoryByUserId(Long userId);
}
