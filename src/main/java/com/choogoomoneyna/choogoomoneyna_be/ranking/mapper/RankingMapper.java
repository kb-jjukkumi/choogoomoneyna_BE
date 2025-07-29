package com.choogoomoneyna.choogoomoneyna_be.ranking.mapper;

import com.choogoomoneyna.choogoomoneyna_be.ranking.vo.RankingUpdateVO;
import com.choogoomoneyna.choogoomoneyna_be.ranking.vo.RankingVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RankingMapper {

    void insertRanking(RankingVO rankingVO);

    List<RankingVO> getAllRanking();

    void batchInsertRankings(@Param("rankingVOList") List<RankingVO> rankingVOList);

    void updateCurrentRankingByUserId(@Param("userId") Long userId,
                                      @Param("currentRanking") Integer currentRanking);

    void batchUpdateCurrentRanking(@Param("rankingUpdateList") List<RankingUpdateVO> rankingUpdateList);

    List<RankingVO> findAllRankingByUserId(Long userId);

    Integer findCurrentRankingByUserId(Long userId);
}
