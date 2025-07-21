package com.choogoomoneyna.choogoomoneyna_be.matching.service;

import com.choogoomoneyna.choogoomoneyna_be.matching.vo.MatchingVO;
import com.choogoomoneyna.choogoomoneyna_be.user.vo.MatchedUserVO;

import java.util.List;

public interface MatchingService {

    /**
     * 모든 사용자의 매칭을 시작합니다
     * 모든 사용자들을 둘씩 매칭 시킨 후 db 에 저장합니다
     */
    public void startAllMatching();

    /**
     * 특정 사용자의 매칭을 시작합니다
     * dummy data 와 매칭 시켜줍니다
     * 이후 db 에 저장됩니다
     * 
     * @param userId 매칭될 사용자의 ID
     */
    public void startMatching(Long userId);

    /**
     * 모든 사용자의 매칭을 종료합니다
     */
    public void finishAllMatching();

    /**
     * 매칭 상태를 업데이트합니다
     *
     * @param matchingId     업데이트할 매칭의 ID
     * @param matchingStatus 설정할 매칭 상태
     */
    public void updateMatchingStatus(Long matchingId, String matchingStatus);

    /**
     * 매칭 상태를 조회합니다
     *
     * @param matchingId 조회할 매칭의 ID
     * @return 매칭 상태
     */
    public String getMatchingStatus(Long matchingId);

    /**
     * 특정 사용자의 최근 매칭 기록을 조회합니다
     *
     * @param userId 조회할 사용자의 ID
     * @param limit 매칭 기록을 가져올 개수
     * @return 해당 사용자의 모든 매칭 정보 목록
     */
    public List<MatchingVO> findRecentNMatchingsByUserId(Long userId, int limit);

    /**
     * 특정 사용자의 모든 매칭 기록을 조회합니다
     *
     * @param userId 조회할 사용자의 ID
     * @return 해당 사용자의 모든 매칭 정보 목록
     */
    public List<MatchingVO> findAllMatchingsByUserId(Long userId);
}
