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
    RankingVO findRankingByUserId(Long userId);

    /**
     * 사용자 ID를 기반으로 현재 랭킹을 조회합니다.
     *
     * @param userId 현재 랭킹을 조회할 사용자의 고유 식별자
     * @return 사용자의 현재 랭킹, 랭킹이 없는 경우 null 반환
     */
    Integer getCurrentRanking(Long userId);

    /**
     * 현재 주간 랭킹을 이전 주간 랭킹으로 이월합니다.
     * 이 메서드는 새로운 주간 랭킹 계산을 위해 랭킹을 보관하거나
     * 전환하는 역할을 담당합니다.
     * <p>
     * 이 작업은 일반적으로 랭킹 업데이트 프로세스의 일부로
     * 새로운 주가 시작될 때 실행됩니다.
     */
    void rolloverWeeklyRankings();

    /**
     * 여러 사용자의 현재 랭킹을 일괄 업데이트합니다.
     * 업데이트할 랭킹은 제공된 RankingUpdateVO 객체 리스트에 명시됩니다.
     *
     * @param rankingList 사용자 ID와 적용할 새로운 현재 랭킹이 포함된
     *                    RankingUpdateVO 객체 리스트
     */
    void batchUpdateCurrentRanks(List<RankingUpdateVO> rankingList);

    /**
     * 모든 사용자의 현재 점수를 기반으로 순위를 다시 계산하여 랭킹을 업데이트합니다.
     * 이 메서드는 다음 작업을 수행합니다:
     * 1. 현재 주간 랭킹을 이전 랭킹으로 이월합니다.
     * 2. 모든 사용자의 점수를 조회하고, 점수를 기준으로 내림차순 정렬하여 순위를 할당합니다.
     * 3. 효율성 향상을 위해 새로 계산된 랭킹을 일괄 업데이트합니다.
     * <p>
     * 이 메서드는 트랜잭션으로 처리되어 모든 작업이 성공하거나 실패 시 전체 작업이 취소됩니다.
     */
    void updateRanking();
}
