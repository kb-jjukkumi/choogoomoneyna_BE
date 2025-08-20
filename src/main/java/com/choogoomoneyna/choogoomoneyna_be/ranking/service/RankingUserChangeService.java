package com.choogoomoneyna.choogoomoneyna_be.ranking.service;

import com.choogoomoneyna.choogoomoneyna_be.ranking.vo.RankingUserChangeVO;

import java.util.List;

public interface RankingUserChangeService {

    /**
     * 특정 라운드 번호에 해당하는 상위 N명의 사용자의 랭킹 변동 정보를 조회합니다.
     *
     * @param roundNumber 랭킹 변동 정보를 조회할 라운드 번호
     * @param limit       조회할 최대 사용자 수
     * @return 해당 라운드의 상위 N명의 랭킹 변동 정보를 담은 {@link RankingUserChangeVO} 객체 리스트,
     * 해당 라운드에 데이터가 없는 경우 빈 리스트 반환
     */
    List<RankingUserChangeVO> findTopNRankingUserChangeByRoundNumber(int roundNumber, int limit);
}
