package com.choogoomoneyna.choogoomoneyna_be.survey.service;

import com.choogoomoneyna.choogoomoneyna_be.survey.dto.request.SurveyRequestDTO;
import com.choogoomoneyna.choogoomoneyna_be.survey.vo.SurveyResponseVO;

import java.util.Date;
import java.util.List;

public class SurveyConverter {

    // SurveyRequestDTO -> List<SurveyResponseVO>
    public static List<SurveyResponseVO> toSurveyResponseVOList(Long userId, SurveyRequestDTO dto) {
        return dto.getSurveyAnswers().stream()
                .map(answer -> SurveyResponseVO.builder()
                        .userId(userId)
                        .surveyQuestionId(answer.getSurveyQuestionId())
                        .surveyOptionId(String.valueOf(answer.getSurveyAnswerId()))
                        .regDate(new Date())
                        .build())
                .toList();
    }
}
