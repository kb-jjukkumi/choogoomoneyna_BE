package com.choogoomoneyna.choogoomoneyna_be.ranking.mapper;

import com.choogoomoneyna.choogoomoneyna_be.ranking.vo.RankingVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RankingMapper {

    void insertRanking(RankingVO rankingVO);

    void updateCurrentRankingByUserId(@Param("userId") Long userId,
                                      @Param("currentRank") Integer currentRank);

    RankingVO findRankingByUserId(Long userId);

    Integer getCurrentRanking(Long userId);

    void rolloverWeeklyRankings();
}
