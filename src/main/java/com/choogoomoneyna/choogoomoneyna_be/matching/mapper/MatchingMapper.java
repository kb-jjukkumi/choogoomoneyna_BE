package com.choogoomoneyna.choogoomoneyna_be.matching.mapper;

import com.choogoomoneyna.choogoomoneyna_be.matching.vo.MatchingVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MatchingMapper {
    void insertMatching(MatchingVO matchingVO);

    List<MatchingVO> getAllProgressMatchings();

    void updateAllProgressMatchings();

    List<MatchingVO> getRecentNMatchingsByUserId(@Param("userId") Long userId, @Param("limit") int limit);

    List<MatchingVO> getAllMatchingsByUserId(Long userId);

    String getMatchingStatus(Long matchingId);

    void updateMatchingStatus(Long matchingId, String matchingStatus);
}
