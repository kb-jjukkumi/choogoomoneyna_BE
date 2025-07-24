package com.choogoomoneyna.choogoomoneyna_be.matching.controller;

import com.choogoomoneyna.choogoomoneyna_be.auth.jwt.CustomUserDetails;
import com.choogoomoneyna.choogoomoneyna_be.matching.dto.Request.MatchingMissionUpdateRequest;
import com.choogoomoneyna.choogoomoneyna_be.matching.dto.Response.MatchingMainResponseDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @PutMapping("/missions/complete")
    public ResponseEntity<?> completeMatchingMission(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody MatchingMissionUpdateRequest request) {

        Long userId = userDetails.getId();

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
                matchingMissionResultService.getAllMissionProgressDTO(userId, matchId);

        int updateScore = scoreService.getScore(userId) + score;
//        scoreService.updateScore(
//                UserScoreVO.builder()
//                        .userId(userId)
//                        .score(updateScore)
//                        .build()
//        );

        return ResponseEntity.ok(
                MissionProgressListResponseDTO.builder()
                        .message("Mission progress updated")
                        .missionProgressDTOList(progressList)
                        .resultScore(updateScore)
                        .build()
        );
    }

    @GetMapping("/detail")
    public ResponseEntity<?> getMatchingMissionProgress(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getId();

        Long matchId = matchingService.getProgressMatchingIdByUserId(userId);
        if (matchId == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "matching progress not found"));
        }

        List<MissionProgressDTO> progressList =
                matchingMissionResultService.getAllMissionProgressDTO(userId, matchId);

        return ResponseEntity.ok(
                MissionProgressListResponseDTO.builder()
                        .message("Mission progress detail")
                        .missionProgressDTOList(progressList)
                        .build()
        );
    }

    @GetMapping("/main")
    public ResponseEntity<?> getMatchingMain(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getId();

        Long matchId = matchingService.getProgressMatchingIdByUserId(userId);
        if (matchId == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "matching progress not found"));
        }

        Long opponentUserId = matchingService.getComponentUserIdByUserId(userId);
        if (opponentUserId == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "opponent user not found"));
        }

        List<MissionProgressDTO> myProgressList =
                matchingMissionResultService.getAllMissionProgressDTO(userId, matchId);
        List<MissionProgressDTO> opponentProgressList =
                matchingMissionResultService.getAllMissionProgressDTO(opponentUserId, matchId);

        return ResponseEntity.ok(
                MatchingMainResponseDTO.builder()
                        .message("Matching main detail")
                        .myMissionProgressList(myProgressList)
                        .opponentMissionProgressList(opponentProgressList)
                        .build()
        );
    }
}
