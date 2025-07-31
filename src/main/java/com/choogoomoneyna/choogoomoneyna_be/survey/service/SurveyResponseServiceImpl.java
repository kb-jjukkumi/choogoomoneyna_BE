package com.choogoomoneyna.choogoomoneyna_be.survey.service;

import com.choogoomoneyna.choogoomoneyna_be.survey.mapper.SurveyResponseMapper;
import com.choogoomoneyna.choogoomoneyna_be.survey.vo.SurveyResponseVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SurveyResponseServiceImpl implements SurveyResponseService {

    private final SurveyResponseMapper surveyResponseMapper;

    @Override
    public void insertSurveyResponse(SurveyResponseVO surveyResponseVO) {
        surveyResponseMapper.insertSurveyResponse(surveyResponseVO);
    }

    @Override
    public void insertBatchSurveyResponse(List<SurveyResponseVO> surveyResponseVOList) {
        surveyResponseMapper.insertBatchSurveyResponse(surveyResponseVOList);
    }

    @Override
    public List<SurveyResponseVO> findSurveyResponseByUserId(Long userId) {
        return surveyResponseMapper.findSurveyResponseByUserId(userId);
    }
}
