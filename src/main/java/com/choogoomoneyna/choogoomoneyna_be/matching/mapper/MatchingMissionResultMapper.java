package com.choogoomoneyna.choogoomoneyna_be.matching.mapper;

import com.choogoomoneyna.choogoomoneyna_be.matching.vo.MatchingMissionResultVO;
import com.choogoomoneyna.choogoomoneyna_be.mission.vo.UserMissionVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MatchingMissionResultMapper {

    void insertAll(List<MatchingMissionResultVO> matchingMissionResultVOList);

    void insertOne(MatchingMissionResultVO matchingMissionResultVO);

    int updateOne(MatchingMissionResultVO matchingMissionResultVO);

    List<MatchingMissionResultVO> findMatchingMissionByUserId(Long UserId);

    MatchingMissionResultVO findMatchingMissionResultByUserIdAndMatchingIdAndMissionId(
            @Param("userId") Long userId,
            @Param("matchingId") Long matchingId,
            @Param("missionId") Integer missionId
    );

    // 없으면 0 반환
    int getAllScoreByUserIdAndMatchingId(
            @Param("userId") Long userId,
            @Param("matchingId") Long matchingId
    );

    List<MatchingMissionResultVO> findAllMatchingMissionResultByUserIdAndMatchingId(
            @Param("userId") Long UserId,
            @Param("matchingId") Long matchingId
    );

    List<UserMissionVO> getAllUserMissionByUserIdAndMatchingId(
            @Param("userId") Long UserId,
            @Param("matchingId") Long matchingId
    );

    List<Integer> findMissionIdsByUserIdAndMatchingId(
            @Param("userId") Long userId,
            @Param("matchingId") Long matchingId
    );

}
