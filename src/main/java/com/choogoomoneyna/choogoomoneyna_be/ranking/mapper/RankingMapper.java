package com.choogoomoneyna.choogoomoneyna_be.ranking.mapper;

import com.choogoomoneyna.choogoomoneyna_be.ranking.vo.RankingUpdateVO;
import com.choogoomoneyna.choogoomoneyna_be.ranking.vo.RankingVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RankingMapper {

    void insertRanking(RankingVO rankingVO);

    void updateCurrentRankingByUserId(@Param("userId") Long userId,
                                      @Param("currentRanking") Integer currentRanking);

    RankingVO findRankingByUserId(Long userId);

    Integer getCurrentRanking(Long userId);

    void rolloverWeeklyRankings();

    void batchUpdateCurrentRanks(@Param("rankingList") List<RankingUpdateVO> rankingList);
}
