package com.choogoomoneyna.choogoomoneyna_be.survey.service;

import com.choogoomoneyna.choogoomoneyna_be.survey.vo.SurveyResponseVO;

import java.util.List;

public interface SurveyResponseService {

    /**
     * 새로운 설문 응답을 시스템에 삽입합니다.
     *
     * @param surveyResponseVO SurveyResponseVO 객체에 포함된 삽입할 설문 응답 데이터
     */
    void insertSurveyResponse(SurveyResponseVO surveyResponseVO);

    /**
     *
     * @param surveyResponseVOList
     */
    void insertBatchSurveyResponse(List<SurveyResponseVO> surveyResponseVOList);

    /**
     * 제공된 사용자 ID를 기반으로 특정 사용자와 관련된 설문 응답 목록을 조회합니다.
     *
     * @param userId 설문 응답을 조회할 사용자의 고유 식별자
     * @return 지정된 사용자의 설문 응답을 나타내는 SurveyResponseVO 객체 목록
     */
    List<SurveyResponseVO> findSurveyResponseByUserId(Long userId);
}
