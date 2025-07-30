package com.choogoomoneyna.choogoomoneyna_be.matching.controller;

import com.choogoomoneyna.choogoomoneyna_be.matching.dto.Request.MatchingMissionUpdateRequest;
import com.choogoomoneyna.choogoomoneyna_be.matching.service.MatchingMissionResultService;
import com.choogoomoneyna.choogoomoneyna_be.matching.service.MatchingService;
import com.choogoomoneyna.choogoomoneyna_be.matching.service.RoundInfoService;
import com.choogoomoneyna.choogoomoneyna_be.mission.service.MissionService;
import com.choogoomoneyna.choogoomoneyna_be.score.service.ScoreService;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { /* AppConfig.class 또는 WebConfig.class */ })
public class MatchingMissionResultControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private MatchingMissionResultController controller;

    @Mock
    private MatchingMissionResultService matchingMissionResultService;

    @Mock
    private MissionService missionService;

    @Mock
    private MatchingService matchingService;

    @Mock
    private RoundInfoService roundInfoService;

    @Mock
    private ScoreService scoreService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void test_completeMatchingMission_good() throws Exception {
        // Given
        Long userId = 1L;
        int missionId = 101;
        int score = 30;
        Long matchId = 10L;

        when(matchingService.getProgressMatchingIdByUserId(userId)).thenReturn(matchId);
        when(missionService.getMissionScore(missionId)).thenReturn(score);
        when(scoreService.getScoreByUserIdAndRoundNumber(userId, 1)).thenReturn(70);
        when(matchingMissionResultService.getMatchingMissionResults(userId, matchId))
                .thenReturn(new ArrayList<>());

        MatchingMissionUpdateRequest request = new MatchingMissionUpdateRequest();
        request.setMissionId(missionId);

        // When & Then
        mockMvc.perform(put("/api/matching/missions/{userId}/complete", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Mission progress updated"))
                .andExpect(jsonPath("$.resultScore").value(100));
    }

    @Test
    public void test_completeMatchingMission_bad() throws Exception {
        Long userId = 1L;

        when(matchingService.getProgressMatchingIdByUserId(userId)).thenReturn(null);

        MatchingMissionUpdateRequest request = new MatchingMissionUpdateRequest();
        request.setMissionId(101);

        mockMvc.perform(put("/api/matching/missions/{userId}/complete", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("matching progress not found"));
    }
}
