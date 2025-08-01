package com.choogoomoneyna.choogoomoneyna_be.ranking.mapper;

import com.choogoomoneyna.choogoomoneyna_be.ranking.vo.RankingHistoryVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RankingHistoryMapper {

    List<RankingHistoryVO> :findAllRankingHistoryByUserId(Long userId);
}
