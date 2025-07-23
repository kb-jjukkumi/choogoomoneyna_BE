package com.choogoomoneyna.choogoomoneyna_be.matching.service;

import com.choogoomoneyna.choogoomoneyna_be.matching.vo.RoundInfoVO;

import java.util.Date;

public interface RoundInfoService {
    
    /**
     * 새로운 라운드 정보를 생성합니다
     *
     * @param roundInfoVO 생성할 라운드 정보가 담긴 객체
     */
    void createRoundInfo(RoundInfoVO roundInfoVO);

    /**
     * 현재 라운드 정보를 조회합니다
     *
     * @return 현재 라운드 정보 객체
     */
    RoundInfoVO getLatestRoundInfo();

    /**
     * 현재 라운드 번호를 조회합니다
     *
     * @return 현재 라운드 번호
     */
    Integer getRoundNumber(Integer roundNumber);

    /**
     * 현재 라운드의 시작 날짜를 조회합니다
     *
     * @param roundNumber 조회할 라운드 번호
     * @return 현재 라운드의 시작 날짜
     */
    Date getStartDate(Integer roundNumber);

    /**
     * 현재 라운드의 종료 날짜를 조회합니다
     *
     * @param roundNumber 조회할 라운드 번호
     * @return 현재 라운드의 종료 날짜
     */
    Date getEndDate(Integer roundNumber);
}
