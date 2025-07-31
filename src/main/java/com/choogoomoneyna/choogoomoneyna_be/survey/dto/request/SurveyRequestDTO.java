package com.choogoomoneyna.choogoomoneyna_be.survey.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SurveyRequestDTO {
    @NotEmpty(message = "설문 응답은 필수입니다.")
    private List<@Valid SingleSurveyAnswerDTO> surveyAnswers;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SingleSurveyAnswerDTO {
        @NotNull(message = "질문 ID는 필수입니다.")
        private Integer surveyQuestionId;

        @NotNull(message = "답변 ID는 필수입니다.")
        private Integer surveyAnswerId;
    }
}

