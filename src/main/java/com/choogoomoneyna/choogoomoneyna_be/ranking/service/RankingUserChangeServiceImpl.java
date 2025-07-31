package com.choogoomoneyna.choogoomoneyna_be.ranking.service;

import com.choogoomoneyna.choogoomoneyna_be.ranking.mapper.RankingUserChangeMapper;
import com.choogoomoneyna.choogoomoneyna_be.ranking.vo.RankingUserChangeVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RankingUserChangeServiceImpl implements RankingUserChangeService {

    private final RankingUserChangeMapper rankingUserChangeMapper;

    @Override
    public List<RankingUserChangeVO> findTopNRankingUserChangeByRoundNumber(int roundNumber, int limit) {
        return rankingUserChangeMapper.findTopNRankingUserChangeByRoundNumber(roundNumber, limit);
    }
}
