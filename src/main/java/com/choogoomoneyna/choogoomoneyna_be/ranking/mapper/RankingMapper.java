package com.choogoomoneyna.choogoomoneyna_be.ranking.mapper;

import com.choogoomoneyna.choogoomoneyna_be.ranking.vo.RankingVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RankingMapper {

    void insertRanking(RankingVO rankingVO);

    RankingVO findRankingByUserId(Long userId);

    Integer getCurrentRanking(Long userId);
}
