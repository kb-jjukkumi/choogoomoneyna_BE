package com.choogoomoneyna.choogoomoneyna_be.matching.mapper;

import com.choogoomoneyna.choogoomoneyna_be.matching.vo.MatchingMissionResultVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MatchingMissionResultMapper {

    void insertAll(List<MatchingMissionResultVO> matchingMissionResultVOList);

    void insertOne(MatchingMissionResultVO matchingMissionResultVO);

    void updateOne(MatchingMissionResultVO matchingMissionResultVO);

    List<MatchingMissionResultVO> findMatchingMissionByUserId(Long UserId);

    MatchingMissionResultVO findMatchingMissionResultByUserIdAndMatchingIdAndMissionId(
            @Param("userId") Long userId,
            @Param("matchingId") Long matchingId,
            @Param("missionId") Integer missionId
    );

}
