package com.choogoomoneyna.choogoomoneyna_be.ranking.service;

import com.choogoomoneyna.choogoomoneyna_be.ranking.vo.RankingUpdateVO;
import com.choogoomoneyna.choogoomoneyna_be.ranking.vo.RankingVO;

import java.util.List;

public interface RankingService {

    /**
     * 제공된 랭킹 정보를 사용하여 시스템에 새로운 랭킹 항목을 생성합니다.
     *
     * @param rankingVO 생성할 랭킹 정보(사용자 ID, 이전 랭킹, 현재 랭킹 포함)
     */
    void createRanking(RankingVO rankingVO);

    void batchInsertRankings(List<RankingVO> rankingVOList);

    /**
     * 사용자의 고유 ID를 기반으로 현재 랭킹을 업데이트합니다.
     *
     * @param userId         랭킹을 업데이트할 사용자의 고유 식별자
     * @param currentRanking 사용자에게 할당될 새로운 현재 랭킹
     */
    void updateCurrentRankingByUserId(Long userId, int currentRanking);

    /**
     * 사용자 ID를 기반으로 랭킹 정보를 조회합니다.
     *
     * @param userId 랭킹 정보를 조회할 사용자의 고유 식별자
     * @return 사용자의 랭킹 정보를 담고 있는 {@link RankingVO} 객체,
     * 랭킹 데이터가 없는 경우 null 반환
     */
    List<RankingVO> findAllRankingByUserId(Long userId);

    /**
     * 사용자 ID를 기반으로 현재 랭킹을 조회합니다.
     *
     * @param userId 현재 랭킹을 조회할 사용자의 고유 식별자
     * @return 사용자의 현재 랭킹, 랭킹이 없는 경우 null 반환
     */
    Integer findCurrentRankingByUserId(Long userId);

    /**
     * 여러 사용자의 현재 랭킹을 일괄 업데이트합니다.
     * 업데이트할 랭킹은 제공된 RankingUpdateVO 객체 리스트에 명시됩니다.
     *
     * @param rankingList 사용자 ID와 적용할 새로운 현재 랭킹이 포함된
     *                    RankingUpdateVO 객체 리스트
     */
    void batchUpdateCurrentRanking(List<RankingUpdateVO> rankingList);
}
