package com.choogoomoneyna.choogoomoneyna_be.matching.service;

import com.choogoomoneyna.choogoomoneyna_be.account.db.service.AccountDbService;
import com.choogoomoneyna.choogoomoneyna_be.matching.dto.Response.TextAiResponseDTO;
import com.choogoomoneyna.choogoomoneyna_be.matching.mapper.MatchingMissionResultMapper;
import com.choogoomoneyna.choogoomoneyna_be.matching.service.opneAi.MissionAiService;
import com.choogoomoneyna.choogoomoneyna_be.matching.vo.MatchingMissionResultVO;
import com.choogoomoneyna.choogoomoneyna_be.mission.service.MissionService;
import com.choogoomoneyna.choogoomoneyna_be.mission.vo.UserMissionVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MatchingMissionResultServiceImplTest {

    @Mock private MatchingMissionResultMapper matchingMissionResultMapper;
    @Mock private MissionAiService missionAiService;
    @Mock private AccountDbService accountDbService;
    @Mock private MissionService missionService;

    @InjectMocks
    private MatchingMissionResultServiceImpl matchingMissionResultService;

    private final Long userId = 1L;
    private final Long matchingId = 1L;
    private final Integer roundNumber = 1;
    private final Integer missionId = 100;
    private final Integer missionScore = 1000;

    @Test
    void createMatchingMissionResult_insertVO() {
        // given
        MatchingMissionResultVO vo = MatchingMissionResultVO.builder()
                .userId(userId)
                .matchingId(matchingId)
                .missionId(missionId)
                .resultScore(missionScore)
                .build();

        // when
        matchingMissionResultService.createMatchingMissionResult(userId, matchingId, missionId);

        // then
        verify(matchingMissionResultMapper, times(1)).insertOne(any(MatchingMissionResultVO.class));
    }

    @Test
    void updateMatchingMissionResult_updateScore() {
        // given
        int existingScore = 100;
        MatchingMissionResultVO existingVO = MatchingMissionResultVO.builder()
                .id(1L)
                .userId(userId)
                .matchingId(matchingId)
                .missionId(missionId)
                .resultScore(existingScore)
                .build();

        when(matchingMissionResultMapper.findMatchingMissionResultByUserIdAndMatchingIdAndMissionId(userId, matchingId, missionId))
                .thenReturn(existingVO);

        when(matchingMissionResultMapper.updateOne(existingVO)).thenReturn(1);

        // when
        matchingMissionResultService.updateMatchingMissionResult(userId, matchingId, missionId, missionScore);

        // then
        assertEquals(existingScore + missionScore, existingVO.getResultScore());
        verify(matchingMissionResultMapper, times(1)).updateOne(existingVO);
    }

    @Test
    void validateMissionType3_success() {
        // given
        String contents = "test content";
        String missionTitle = "title";
        String missionContent = "content";

        // mission 제목 및 내용
        when(missionService.getMissionTitle(missionId)).thenReturn(missionTitle);
        when(missionService.getMissionContent(missionId)).thenReturn(missionContent);

        // 검증
        TextAiResponseDTO responseDTO = new TextAiResponseDTO();
        responseDTO.setScore(80);  // 70 점 이상으로 성공
        when(missionAiService.validateMission3(anyString(), eq(contents))).thenReturn(responseDTO);

        // 기존 VO stub
        MatchingMissionResultVO existingVO = MatchingMissionResultVO.builder()
                .id(1L)
                .userId(userId)
                .matchingId(matchingId)
                .missionId(missionId)
                .resultScore(0)
                .build();
        when(matchingMissionResultMapper.findMatchingMissionResultByUserIdAndMatchingIdAndMissionId(userId, matchingId, missionId))
                .thenReturn(existingVO);

        // when
        matchingMissionResultService.validateMissionType3(userId, matchingId, missionId, contents, missionScore);

        // then
        verify(matchingMissionResultMapper, times(1)).updateOne(any(MatchingMissionResultVO.class));
    }

    @Test
    void validateMissionType3_fail() {
        // given
        String contents = "test content";
        String missionTitle = "title";
        String missionContent = "content";

        when(missionService.getMissionTitle(missionId)).thenReturn(missionTitle);
        when(missionService.getMissionContent(missionId)).thenReturn(missionContent);

        TextAiResponseDTO responseDTO = new TextAiResponseDTO();
        responseDTO.setScore(50);  // 70 점 미만으로 실패
        when(missionAiService.validateMission3(anyString(), eq(contents))).thenReturn(responseDTO);

        // when
        matchingMissionResultService.validateMissionType3(userId, matchingId, missionId, contents, missionScore);

        // then
        verify(matchingMissionResultMapper, never()).updateOne(any(MatchingMissionResultVO.class));
    }

    @Test
    void getAllScoreByUserIdAndMatchingId_returnsScore() {
        // given
        List<MatchingMissionResultVO> mockResults = Arrays.asList(
                MatchingMissionResultVO.builder()
                        .id(1L)
                        .userId(userId)
                        .matchingId(matchingId)
                        .missionId(101)
                        .resultScore(50)
                        .build(),
                MatchingMissionResultVO.builder()
                        .id(2L)
                        .userId(userId)
                        .matchingId(matchingId)
                        .missionId(102)
                        .resultScore(30)
                        .build()
        );

        when(matchingMissionResultMapper.findAllMatchingMissionResultByUserIdAndMatchingId(userId, matchingId))
                .thenReturn(mockResults);

        // when
        List<MatchingMissionResultVO> actualMissions =
                matchingMissionResultService.getMatchingMissionResults(userId, matchingId);

        // then
        assertEquals(2, actualMissions.size());
        assertIterableEquals(mockResults, actualMissions);  // 동일한 순서를 가지는지 확인
        verify(matchingMissionResultMapper, times(1))
                .findAllMatchingMissionResultByUserIdAndMatchingId(userId, matchingId);
    }
}