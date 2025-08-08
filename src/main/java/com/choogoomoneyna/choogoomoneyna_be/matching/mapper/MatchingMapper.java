package com.choogoomoneyna.choogoomoneyna_be.matching.mapper;

import com.choogoomoneyna.choogoomoneyna_be.matching.vo.MatchingVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MatchingMapper {
    void insertMatching(MatchingVO matchingVO);

    List<MatchingVO> findAllProgressMatchings();

    void updateAllProgressMatchings();

    List<MatchingVO> findRecentNMatchingsByUserId(@Param("userId") Long userId, @Param("limit") int limit);

    List<MatchingVO> findAllMatchingsByUserId(Long userId);

    MatchingVO findMatchingByMatchingId(Long matchingId);

    String findMatchingStatus(Long matchingId);

    void updateMatchingStatus(Long matchingId, String matchingStatus);

    Long getProgressMatchingIdByUserId(Long userId);

    Long getProgressMatchingIdByUserIdAndRoundNumber(
            @Param("userId") Long userId,
            @Param("roundNumber") Integer roundNumber
    );

    Long getComponentUserIdByUserIdAndMatchingId(
            @Param("userId") Long userId,
            @Param("matchingId") Long matchingId
    );

    List<Long> findAllUserIdInProgressMatching();
}
