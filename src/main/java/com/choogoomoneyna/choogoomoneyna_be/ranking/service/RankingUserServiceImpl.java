package com.choogoomoneyna.choogoomoneyna_be.ranking.service;

import com.choogoomoneyna.choogoomoneyna_be.ranking.mapper.RankingUserMapper;
import com.choogoomoneyna.choogoomoneyna_be.ranking.vo.RankingUserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RankingUserServiceImpl implements RankingUserService {

    private final RankingUserMapper rankingUserMapper;

    @Override
    public List<RankingUserVO> getAllRankingUser() {
        return rankingUserMapper.getAllRankingUser();
    }

    @Override
    public List<RankingUserVO> findLatestRankingUserPerUser() {
        return rankingUserMapper.findLatestRankingUserPerUser();
    }
}
