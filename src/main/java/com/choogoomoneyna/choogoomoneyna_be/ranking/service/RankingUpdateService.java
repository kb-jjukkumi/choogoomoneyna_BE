package com.choogoomoneyna.choogoomoneyna_be.ranking.service;

public interface RankingUpdateService {

    /**
     * 시스템의 최신 데이터를 기반으로 랭킹을 재계산하여 업데이트합니다.
     * 이 메소드는 시스템의 랭킹 규칙과 로직에 따라 랭킹 정보가 일관되고
     * 정확하게 유지되도록 랭킹 새로고침을 트리거하는 데 사용됩니다.
     */
    void updateRanking();

    /**
     * 시스템에 새로운 주간 랭킹을 생성합니다.
     * 이 메소드는 구현에 정의된 특정 로직이나 기준에 따라 현재 주의 랭킹을
     * 초기화하는 것을 목적으로 합니다. 이전 주간 데이터를 정리하고 새로운
     * 랭킹이 계산되거나 저장될 수 있도록 준비함으로써, 새로운 기간의 시작을
     * 반영하도록 주간 랭킹이 리셋되거나 업데이트되도록 보장합니다.
     */
    void createNewWeekRankings();
}
