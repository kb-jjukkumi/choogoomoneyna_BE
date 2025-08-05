package com.choogoomoneyna.choogoomoneyna_be.mission.service;

public interface MissionService {

    /**
     * 미션의 만점 점수를 조회하는 함수
     * @param missionId
     * @return 미션 만점 점수
     */
    public Integer getMissionScore(Integer missionId);

    /**
     * CODEF_WEEKLY, CODEF_DAILY 미션의 지출 제한 금액을 조회하는 함수
     * @param missionId
     * @return 지출 제한 금액
     */
    public Integer getMissionLimitAmount(Integer missionId);
}
