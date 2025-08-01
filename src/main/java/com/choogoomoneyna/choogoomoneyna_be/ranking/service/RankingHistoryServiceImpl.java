package com.choogoomoneyna.choogoomoneyna_be.ranking.service;

import com.choogoomoneyna.choogoomoneyna_be.ranking.mapper.RankingHistoryMapper;
import com.choogoomoneyna.choogoomoneyna_be.ranking.vo.RankingHistoryVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RankingHistoryServiceImpl implements RankingHistoryService {

    private final RankingHistoryMapper rankingHistoryMapper;

    @Override
    public List<RankingHistoryVO> findAllRankingHistoryByUserId(Long userId) {
        return rankingHistoryMapper.findAllRankingHistoryByUserId(userId);
    }
}
