package com.choogoomoneyna.choogoomoneyna_be.ranking.service;

import com.choogoomoneyna.choogoomoneyna_be.ranking.mapper.RankingMapper;
import com.choogoomoneyna.choogoomoneyna_be.ranking.vo.RankingUpdateVO;
import com.choogoomoneyna.choogoomoneyna_be.score.service.ScoreService;
import com.choogoomoneyna.choogoomoneyna_be.score.vo.UserScoreVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class RankingServiceImplTest {

    @Mock
    private RankingMapper rankingMapper;

    @InjectMocks
    private RankingServiceImpl rankingService; // 목 객체들 주입된 구현체

    @Mock
    private ScoreService scoreService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void updateRanking() {
        // given
        List<UserScoreVO> userScores = List.of(
                new UserScoreVO(1L, 100),
                new UserScoreVO(2L, 100),
                new UserScoreVO(3L, 70),
                new UserScoreVO(4L, 70),
                new UserScoreVO(5L, 90)
        );

        given(scoreService.getAllScores()).willReturn(userScores);

        // when
        rankingService.updateRanking();

        // then
        // 1) rolloverWeeklyRankings()가 1회 호출
        verify(rankingMapper, times(1)).rolloverWeeklyRankings();

        // 2) batchUpdateCurrentRanks()가 1회 호출되고, 랭킹 계산이 올바른지 ArgumentCaptor로 확인
        ArgumentCaptor<List<RankingUpdateVO>> captor = ArgumentCaptor.forClass(List.class);
        verify(rankingMapper, times(1)).batchUpdateCurrentRanks(captor.capture());

        List<RankingUpdateVO> capturedList = captor.getValue();
        assertEquals(5, capturedList.size());

        // 정렬 기준: 점수 내림차순, 동점자 동순위
        assertEquals(1, capturedList.get(0).getCurrentRanking());
        assertEquals(1, capturedList.get(1).getCurrentRanking());
        assertEquals(3, capturedList.get(2).getCurrentRanking());
        assertEquals(4, capturedList.get(3).getCurrentRanking());
        assertEquals(4, capturedList.get(4).getCurrentRanking());
    }

}