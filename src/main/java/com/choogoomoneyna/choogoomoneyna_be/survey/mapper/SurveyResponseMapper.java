package com.choogoomoneyna.choogoomoneyna_be.survey.mapper;

import com.choogoomoneyna.choogoomoneyna_be.survey.vo.SurveyResponseVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SurveyResponseMapper {

    void insertBatchSurveyResponse(List<SurveyResponseVO> surveyResponseVOList);

    List<SurveyResponseVO> findLatestSurveyResponseByUserId(Long userId);
}
