package com.choogoomoneyna.choogoomoneyna_be.ranking.service;

import com.choogoomoneyna.choogoomoneyna_be.ranking.vo.RankingUserVO;
import com.choogoomoneyna.choogoomoneyna_be.ranking.vo.RankingVO;

import java.util.List;

public interface RankingUserService {

    /**
     * 시스템의 모든 랭킹 항목을 조회합니다.
     *
     * @return 랭킹 ID, 사용자 ID, 현재 랭킹, 등록일, 수정일이 포함된
     * {@link RankingVO} 객체 리스트를 반환하며, 랭킹이 없는 경우 빈 리스트를 반환합니다.
     */
    List<RankingUserVO> getAllRankingUser();
    
    /**
     * 특정 라운드 번호에 대해 상위 N명의 랭킹 사용자를 조회합니다.
     * 결과는 해당 라운드의 사용자 랭킹을 기준으로 정렬됩니다.
     *
     * @param roundNumber 랭킹 데이터를 필터링할 라운드 번호
     * @param limit 조회할 상위 랭킹 사용자의 최대 수
     * @return 지정된 라운드의 상위 랭킹 사용자들을 나타내는 {@link RankingUserVO} 객체 리스트를 반환합니다.
     *         데이터가 없는 경우 빈 리스트가 반환됩니다.
     */
    List<RankingUserVO> findTopNRankingUserByRoundNumber(int roundNumber, int limit);
}
