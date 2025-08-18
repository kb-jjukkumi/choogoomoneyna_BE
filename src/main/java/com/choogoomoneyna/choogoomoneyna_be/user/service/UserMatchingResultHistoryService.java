package com.choogoomoneyna.choogoomoneyna_be.user.service;

import com.choogoomoneyna.choogoomoneyna_be.user.vo.UserMatchingResultHistoryVO;

import java.util.List;

public interface UserMatchingResultHistoryService {

    /**
     * 주어진 사용자 ID와 관련된 매칭 결과 히스토리 기록 목록을 조회합니다.
     *
     * @param userId 매칭 결과 히스토리를 조회할 사용자의 고유 식별자
     * @return 지정된 사용자 ID와 관련된 매칭 결과 히스토리를 나타내는 
     *         UserMatchingResultHistoryVO 객체 목록
     */
    List<UserMatchingResultHistoryVO> findUserMatchingResultHistoriesByUserId(Long userId);

}
