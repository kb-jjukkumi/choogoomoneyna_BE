package com.choogoomoneyna.choogoomoneyna_be.matching.controller;

import com.choogoomoneyna.choogoomoneyna_be.matching.dto.Request.MatchingMissionUpdateRequest;
import com.choogoomoneyna.choogoomoneyna_be.matching.service.MatchingMissionConverter;
import com.choogoomoneyna.choogoomoneyna_be.matching.service.MatchingMissionResultService;
import com.choogoomoneyna.choogoomoneyna_be.matching.service.MatchingService;
import com.choogoomoneyna.choogoomoneyna_be.matching.service.RoundInfoService;
import com.choogoomoneyna.choogoomoneyna_be.mission.dto.response.MissionProgressDTO;
import com.choogoomoneyna.choogoomoneyna_be.mission.dto.response.MissionProgressListResponseDTO;
import com.choogoomoneyna.choogoomoneyna_be.mission.service.MissionService;
import com.choogoomoneyna.choogoomoneyna_be.score.service.ScoreService;
import com.choogoomoneyna.choogoomoneyna_be.score.vo.UserScoreVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/matching")
public class MatchingMissionResultController {

    private final MatchingMissionResultService matchingMissionResultService;
    private final MissionService missionService;
    private final MatchingService matchingService;
    private final RoundInfoService roundInfoService;
    private final ScoreService scoreService;

    @PutMapping("/update/{userId}/complete")
    public ResponseEntity<?> missionComplete(
            @PathVariable Long userId,
            @RequestBody MatchingMissionUpdateRequest request) {
        Long matchId = matchingService.getProgressMatchingIdByUserId(userId);
        if (matchId == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "matching progress not found"));
        }

        int missionId = request.getMissionId();
        Integer score = missionService.getMissionScore(missionId);
        if (score == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "mission score not found"));
        }

        matchingMissionResultService.updateMatchingMissionResult(
                userId, matchId, missionId, score
        );

        List<MissionProgressDTO> progressList =
                matchingMissionResultService.getAllMissionProgress(userId, matchId)
                        .stream()
                        .map(MatchingMissionConverter::toMissionProgressDTO)
                        .toList();

        int scoreBefore = scoreService.getScore(userId);
        scoreService.updateScore(
                UserScoreVO.builder()
                        .userId(userId)
                        .score(scoreBefore + score)
                        .build()
        );

        return ResponseEntity.ok(
                MissionProgressListResponseDTO.builder()
                        .message("Mission progress updated")
                        .missionProgressDTOList(progressList)
                        .resultScore(scoreBefore + score)
                        .build()
        );
    }
}
