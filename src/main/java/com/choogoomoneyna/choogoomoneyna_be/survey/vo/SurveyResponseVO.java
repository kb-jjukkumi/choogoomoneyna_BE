package com.choogoomoneyna.choogoomoneyna_be.survey.vo;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyResponseVO {
    private Long id;
    private Long userId;
    private Integer surveyQuestionId;
    private String surveyOptionId;
    private Date regDate;
}
