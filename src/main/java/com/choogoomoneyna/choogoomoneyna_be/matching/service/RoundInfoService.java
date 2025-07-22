package com.choogoomoneyna.choogoomoneyna_be.matching.service;

import com.choogoomoneyna.choogoomoneyna_be.matching.vo.RoundInfoVO;

import java.util.Date;

public interface RoundInfoService {

    /**
     * 라운드 정보를 제공된 데이터로 업데이트합니다
     *
     * @param roundInfoVO 업데이트할 라운드 정보가 담긴 객체
     */
    void updateRoundInfo(RoundInfoVO roundInfoVO);

    /**
     * 현재 라운드 정보를 조회합니다
     *
     * @return 현재 라운드 정보 객체
     */
    RoundInfoVO getRoundInfo();

    /**
     * 현재 라운드 번호를 조회합니다
     *
     * @return 현재 라운드 번호
     */
    Integer getRoundNumber();

    /**
     * 현재 라운드의 시작 날짜를 조회합니다
     *
     * @return 현재 라운드의 시작 날짜
     */
    Date getStartDate();

    /**
     * 현재 라운드의 종료 날짜를 조회합니다
     *
     * @return 현재 라운드의 종료 날짜
     */
    Date getEndDate();
}
