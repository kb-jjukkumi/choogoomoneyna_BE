package com.choogoomoneyna.choogoomoneyna_be.ranking.service;

import com.choogoomoneyna.choogoomoneyna_be.ranking.mapper.RankingMapper;
import com.choogoomoneyna.choogoomoneyna_be.ranking.vo.RankingUpdateVO;
import com.choogoomoneyna.choogoomoneyna_be.ranking.vo.RankingVO;
import com.choogoomoneyna.choogoomoneyna_be.score.service.ScoreService;
import com.choogoomoneyna.choogoomoneyna_be.score.vo.UserScoreVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RankingServiceImpl implements RankingService {

    private final RankingMapper rankingMapper;

    @Override
    public void createRanking(RankingVO rankingVO) {
        rankingMapper.insertRanking(rankingVO);
    }

    @Override
    public List<RankingVO> getAllRanking() {
        return rankingMapper.getAllRanking();
    }

    @Override
    public List<RankingVO> findLatestRankingPerUser() {
        return rankingMapper.findLatestRankingPerUser();
    }

    @Override
    public void batchInsertRankings(List<RankingVO> rankingVOList) {
        rankingMapper.batchInsertRankings(rankingVOList);
    }

    @Override
    public void updateCurrentRankingByUserId(Long userId, int currentRanking) {
        rankingMapper.updateCurrentRankingByUserId(userId, currentRanking);
    }

    @Override
    public List<RankingVO> findAllRankingByUserId(Long userId) {
        return rankingMapper.findAllRankingByUserId(userId);
    }

    @Override
    public Integer findCurrentRankingByUserId(Long userId) {
        return rankingMapper.findCurrentRankingByUserId(userId);
    }

    @Override
    public void batchUpdateCurrentRanking(List<RankingUpdateVO> rankingList) {
        rankingMapper.batchUpdateCurrentRanking(rankingList);
    }
}
