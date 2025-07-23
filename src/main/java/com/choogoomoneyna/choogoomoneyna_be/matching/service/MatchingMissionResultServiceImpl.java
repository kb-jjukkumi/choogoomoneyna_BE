package com.choogoomoneyna.choogoomoneyna_be.matching.service;

import com.choogoomoneyna.choogoomoneyna_be.matching.mapper.MatchingMissionResultMapper;
import com.choogoomoneyna.choogoomoneyna_be.matching.vo.MatchingMissionResultVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchingMissionResultServiceImpl implements MatchingMissionResultService {

    private final MatchingMissionResultMapper matchingMissionResultMapper;

    @Override
    public void createMatchingMissionResult(Long userId, Long matchingId, Integer missionId) {
        MatchingMissionResultVO vo = MatchingMissionResultVO.builder()
                .userId(userId)
                .matchingId(matchingId)
                .missionId(missionId)
                .resultScore(0)
                .build();

        matchingMissionResultMapper.insertOne(vo);
    }

    @Override
    public void updateMatchingMissionResult(Long userId, Long matchingId, Integer missionId, Integer missionScore) {
        MatchingMissionResultVO existingVO = matchingMissionResultMapper
                .findMatchingMissionResultByUserIdAndMatchingIdAndMissionId(userId, matchingId, missionId);

        if (existingVO == null) return;

        existingVO.setResultScore(existingVO.getResultScore() + missionScore);
        matchingMissionResultMapper.updateOne(existingVO);
    }

    @Override
    public void updateAllResults() {
        // TODO: 구현 예정
    }

    @Override
    public int getAllScoreByUserIdAndMatchingId(Long userId, Long matchingId) {
        return matchingMissionResultMapper.getAllScoreByUserIdAndMatchingId(userId, matchingId);
    }

    @Override
    public List<MatchingMissionResultVO> getAllMissionProgress(Long userId, Long matchId) {
        return matchingMissionResultMapper.findAllMatchingMissionResultByUserIdAndMatchingId(userId, matchId);
    }
}
