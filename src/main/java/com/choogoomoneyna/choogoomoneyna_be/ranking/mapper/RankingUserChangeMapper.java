package com.choogoomoneyna.choogoomoneyna_be.ranking.mapper;

import com.choogoomoneyna.choogoomoneyna_be.ranking.vo.RankingUserChangeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RankingUserChangeMapper {

    List<RankingUserChangeVO> findTopNRankingUserChangeByRoundNumber(
            @Param("roundNumber") int roundNumber,
            @Param("limit") int limit
    );
}
