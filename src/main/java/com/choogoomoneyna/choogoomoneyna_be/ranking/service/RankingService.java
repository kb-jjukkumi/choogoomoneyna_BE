package com.choogoomoneyna.choogoomoneyna_be.ranking.service;

import com.choogoomoneyna.choogoomoneyna_be.ranking.vo.RankingUpdateVO;
import com.choogoomoneyna.choogoomoneyna_be.ranking.vo.RankingVO;

import java.util.List;

public interface RankingService {

    /**
     * 시스템에 새로운 랭킹 항목을 생성합니다.
     *
     * @param rankingVO 랭킹 ID, 사용자 ID, 현재 랭킹, 등록일,
     *                  수정일 등의 랭킹 정보가 포함된 {@link RankingVO} 인스턴스
     */
    void createRanking(RankingVO rankingVO);

    /**
     * 시스템에 여러 랭킹 항목을 일괄 삽입합니다.
     *
     * @param rankingVOList 사용자 ID, 랭킹 정보, 타임스탬프 등이 포함된
     *                      삽입할 {@link RankingVO} 객체 리스트
     */
    void batchInsertRankings(List<RankingVO> rankingVOList);

    /**
     * 지정된 사용자 ID로 식별되는 사용자의 현재 랭킹을 업데이트합니다.
     *
     * @param userId         랭킹을 업데이트할 사용자의 고유 식별자
     * @param currentRanking 사용자에게 설정할 새로운 랭킹 값
     */
    void updateCurrentRankingByUserId(Long userId, int currentRanking);

    /**
     * 주어진 사용자 ID와 관련된 모든 랭킹 항목 목록을 조회합니다.
     *
     * @param userId 랭킹 기록을 조회할 사용자의 고유 식별자
     * @return 지정된 사용자의 랭킹 정보가 포함된 {@link RankingVO} 객체 리스트,
     *         해당 사용자 ID에 대한 랭킹이 없는 경우 빈 리스트 반환
     */
    List<RankingVO> findAllRankingByUserId(Long userId);

    /**
     * 사용자의 고유 식별자를 기반으로 현재 랭킹을 조회합니다.
     *
     * @param userId 현재 랭킹을 조회할 사용자의 고유 식별자
     * @return 사용자의 현재 랭킹을 {@link Integer}로 반환,
     *         제공된 사용자 ID에 대한 랭킹 데이터가 없는 경우 null 반환
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
