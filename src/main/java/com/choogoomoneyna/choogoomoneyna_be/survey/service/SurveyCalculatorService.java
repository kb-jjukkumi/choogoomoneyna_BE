package com.choogoomoneyna.choogoomoneyna_be.survey.service;

import com.choogoomoneyna.choogoomoneyna_be.user.enums.ChoogooMi;

public interface SurveyCalculatorService {

    /**
     * 설문 응답을 기반으로 사용자에게 가장 적합한 추구미 유형을 추천합니다.
     *
     * @param userId 설문 응답을 분석할 사용자의 고유 식별자
     * @return 사용자에게 추천된 추구미 유형
     */
    ChoogooMi recommendChoogooMi(Long userId);
}
