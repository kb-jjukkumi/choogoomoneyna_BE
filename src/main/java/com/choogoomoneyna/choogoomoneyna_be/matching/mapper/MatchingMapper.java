package com.choogoomoneyna.choogoomoneyna_be.matching.mapper;

import com.choogoomoneyna.choogoomoneyna_be.matching.vo.MatchingVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MatchingMapper {
    void insertMatching(MatchingVO matchingVO);

    List<MatchingVO> findRecentNMatchingsByUserId(@Param("userId") long userId, @Param("limit") int limit);

    List<MatchingVO> findAllMatchingsByUserId(long userId);
    String getMatchingStatus(int matchingId);

    void updateMatchingStatus(int matchingId, String matchingStatus);
}
