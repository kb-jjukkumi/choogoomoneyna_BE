package com.choogoomoneyna.choogoomoneyna_be.user.service;

import com.choogoomoneyna.choogoomoneyna_be.user.vo.UserMainInfoVO;

public interface UserMainInfoService {

    /**
     * 특정 라운드에서 사용자의 메인 응답 정보를 조회합니다.
     *
     * @param userId      정보를 조회할 사용자의 고유 식별자
     * @param roundNumber 조회할 라운드 번호
     * @return UserMainInfoVO 해당 라운드에서 사용자의 메인 정보가 담긴 객체,
     * 사용자를 찾을 수 없는 경우 null 반환
     */
    UserMainInfoVO findUserMainResponseByUserIdAndRoundNumber(Long userId, Integer roundNumber);
}
