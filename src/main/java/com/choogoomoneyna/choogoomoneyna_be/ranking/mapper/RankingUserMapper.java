package com.choogoomoneyna.choogoomoneyna_be.ranking.mapper;

import com.choogoomoneyna.choogoomoneyna_be.ranking.vo.RankingUserVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RankingUserMapper {

    List<RankingUserVO> getAllRankingUser();

    List<RankingUserVO> findLatestRankingUserPerUser();
}
