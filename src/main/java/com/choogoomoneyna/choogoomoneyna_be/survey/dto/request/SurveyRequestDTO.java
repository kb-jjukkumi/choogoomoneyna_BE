package com.choogoomoneyna.choogoomoneyna_be.survey.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SurveyRequestDTO {
    private List<SingleSurveyAnswerDTO> surveyAnswers;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SingleSurveyAnswerDTO {
        private Integer surveyQuestionId;
        private Integer surveyAnswerId;
    }
}
