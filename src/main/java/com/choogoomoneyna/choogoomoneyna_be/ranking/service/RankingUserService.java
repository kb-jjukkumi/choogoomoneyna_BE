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
     * 각 사용자의 최신 랭킹 항목을 조회합니다.
     * 각 사용자당 가장 최근의 랭킹만 포함됩니다.
     *
     * @return {@link RankingVO} 객체 리스트를 반환하며, 데이터가 없는 경우 빈 리스트를 반환합니다.
     */
    List<RankingUserVO> findTop50LatestRankingUserPerUser();

    /**
     * 각 사용자의 두 번째로 최신 랭킹에 기반하여 상위 3명의 사용자를 조회합니다.
     * 등록일 기준으로 정렬됩니다.
     * 사용자가 3명 미만인 경우, 조회 가능한 모든 사용자가 포함됩니다.
     *
     * @return 두 번째로 최신 랭킹 기준으로 정렬된 상위 3명의 {@link RankingUserVO} 객체 리스트를 반환합니다.
     * 사용자가 없는 경우 빈 리스트를 반환합니다.
     */
    List<RankingUserVO> findTop3BySecondLatestRankingByRegDate();
}
