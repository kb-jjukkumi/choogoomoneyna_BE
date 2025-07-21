package com.choogoomoneyna.choogoomoneyna_be.matching.mapper;

import com.choogoomoneyna.choogoomoneyna_be.matching.vo.MatchingMissionResultVO;

import java.util.List;

public interface MatchingMissionResultMapper {

    void insertAll(List<MatchingMissionResultVO> matchingMissionResultVOList);

    void insertOne(MatchingMissionResultVO matchingMissionResultVO);

    List<MatchingMissionResultVO> findMatchingMissionByUserId(Long UserId);
}
