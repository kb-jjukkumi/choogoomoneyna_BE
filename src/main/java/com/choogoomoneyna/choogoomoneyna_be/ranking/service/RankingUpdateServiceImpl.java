package com.choogoomoneyna.choogoomoneyna_be.ranking.service;

import com.choogoomoneyna.choogoomoneyna_be.ranking.vo.RankingUpdateVO;
import com.choogoomoneyna.choogoomoneyna_be.score.service.ScoreService;
import com.choogoomoneyna.choogoomoneyna_be.score.vo.UserScoreVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RankingUpdateServiceImpl implements RankingUpdateService {

    private final RankingService rankingService;
    private final ScoreService scoreService;

    @Transactional
    @Override
    public void updateRanking() {
        List<UserScoreVO> sortedUserScores = new ArrayList<>(scoreService.getAllScores());
        sortedUserScores.sort(Comparator.comparingInt(UserScoreVO::getScore).reversed());

        Map<Long, RankingUpdateVO> latestUpdateMap = new HashMap<>();
        int befScore = Integer.MAX_VALUE;
        int rank = 0;

        for (int idx = 0; idx < sortedUserScores.size(); idx++) {
            UserScoreVO userScore = sortedUserScores.get(idx);
            if (befScore > userScore.getScore()) {
                rank = idx + 1;
                befScore = userScore.getScore();
            }

            RankingUpdateVO vo = RankingUpdateVO.builder()
                    .userId(userScore.getUserId())
                    .currentRanking(rank)
                    .updateDate(new Date()) // 현재 시간으로 설정
                    .build();

            // userId별 최신 updateDate만 유지
            latestUpdateMap.merge(vo.getUserId(), vo, (existing, incoming) ->
                    existing.getUpdateDate().after(incoming.getUpdateDate()) ? existing : incoming
            );
        }

        List<RankingUpdateVO> latestUpdateList = new ArrayList<>(latestUpdateMap.values());

        if (!latestUpdateList.isEmpty()) {
            rankingService.batchUpdateCurrentRanking(latestUpdateList);
        }
    }
}
