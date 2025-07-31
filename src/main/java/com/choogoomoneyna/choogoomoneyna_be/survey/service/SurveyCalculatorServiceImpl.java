package com.choogoomoneyna.choogoomoneyna_be.survey.service;

import com.choogoomoneyna.choogoomoneyna_be.survey.vo.SurveyResponseVO;
import com.choogoomoneyna.choogoomoneyna_be.user.enums.ChoogooMi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SurveyCalculatorServiceImpl implements SurveyCalculatorService {

    private final SurveyResponseService surveyResponseService;

    @Override
    public ChoogooMi recommendChoogooMi(Long userId) {
        List<SurveyResponseVO> userSurveyResponseList = surveyResponseService.findLatestSurveyResponseByUserId(userId);
        return SurveyWeightCalculator.getTopType(userSurveyResponseList);
    }
}
