package com.choogoomoneyna.choogoomoneyna_be.survey.service;

import com.choogoomoneyna.choogoomoneyna_be.survey.vo.SurveyResponseVO;

import java.util.List;

public interface SurveyResponseService {

    /**
     * 여러 개의 설문 응답을 시스템에 일괄 삽입합니다.
     *
     * @param surveyResponseVOList 삽입할 설문 응답 데이터가 포함된 SurveyResponseVO 객체 리스트
     */
    void insertBatchSurveyResponse(List<SurveyResponseVO> surveyResponseVOList);

    /**
     * 제공된 사용자 ID를 기반으로 특정 사용자와 관련된 가장 최근의 설문 응답 목록을 조회합니다.
     *
     * @param userId 설문 응답을 조회할 사용자의 고유 식별자
     * @return 지정된 사용자의 설문 응답을 나타내는 SurveyResponseVO 객체 목록
     */
    List<SurveyResponseVO> findLatestSurveyResponseByUserId(Long userId);
}