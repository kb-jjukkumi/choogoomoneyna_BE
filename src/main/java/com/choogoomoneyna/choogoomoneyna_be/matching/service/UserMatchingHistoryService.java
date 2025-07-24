package com.choogoomoneyna.choogoomoneyna_be.matching.service;

import com.choogoomoneyna.choogoomoneyna_be.matching.vo.UserMatchingHistoryVO;

import java.util.List;

public interface UserMatchingHistoryService {

    /**
     * 사용자 매칭 이력을 데이터베이스에 삽입합니다.
     *
     * @param userMatchingHistoryVO 삽입할 사용자 매칭 이력 정보
     */
    void insertUserMatchingHistory(UserMatchingHistoryVO userMatchingHistoryVO);

    /**
     * 특정 사용자 ID와 관련된 모든 매칭 이력을 조회합니다.
     *
     * @param userId 매칭 이력을 조회할 사용자의 ID
     * @return 특정 사용자의 매칭 이력 목록
     */
    List<UserMatchingHistoryVO> findAllUserMatchingHistoryByUserId(Long userId);
}
