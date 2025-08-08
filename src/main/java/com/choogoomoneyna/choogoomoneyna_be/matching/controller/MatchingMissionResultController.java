package com.choogoomoneyna.choogoomoneyna_be.matching.controller;

import com.choogoomoneyna.choogoomoneyna_be.auth.jwt.util.CustomUserDetails;
import com.choogoomoneyna.choogoomoneyna_be.matching.dto.Request.MatchingMissionQuizUpdateDTO;
import com.choogoomoneyna.choogoomoneyna_be.matching.dto.Request.MatchingMissionUpdateRequest;
import com.choogoomoneyna.choogoomoneyna_be.matching.dto.Response.MatchingMainResponseDTO;
import com.choogoomoneyna.choogoomoneyna_be.matching.service.MatchingDetailService;
import com.choogoomoneyna.choogoomoneyna_be.matching.service.MatchingMissionResultService;
import com.choogoomoneyna.choogoomoneyna_be.matching.service.MatchingService;
import com.choogoomoneyna.choogoomoneyna_be.matching.service.RoundInfoService;
import com.choogoomoneyna.choogoomoneyna_be.mission.dto.response.MissionProgressDTO;
import com.choogoomoneyna.choogoomoneyna_be.mission.dto.response.MissionProgressListResponseDTO;
import com.choogoomoneyna.choogoomoneyna_be.mission.service.MissionService;
import com.choogoomoneyna.choogoomoneyna_be.score.service.ScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private final MatchingDetailService matchingDetailService;

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
        Integer roundNumber = roundInfoService.getRoundNumber();

        MatchingMainResponseDTO response = matchingDetailService.getMatchingDetail(userId, roundNumber);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/history")
    public ResponseEntity<?> getMatchingHistory(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam Integer roundNumber
    ) {
        Long userId = userDetails.getId();

        MatchingMainResponseDTO response = matchingDetailService.getMatchingDetail(userId, roundNumber);
        return ResponseEntity.ok(response);
    }

    @PutMapping("missions/validate/1")
    public ResponseEntity<?> validatemissionType1(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                   @RequestBody MatchingMissionUpdateRequest request) {
        //1. 유저아이디 추출
        Long userId = userDetails.getId();

        //2. 매칭, 미션, 제한 금액 추출
        Long matchingId = matchingService.getProgressMatchingIdByUserId(userId);
        if (matchingId == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "matching progress not found"));
        }
        int missionId = request.getMissionId();
        int limitAmount = missionService.getMissionLimitAmount(missionId);
        int missionScore = missionService.getMissionScore(missionId);

        //3.CODEF_WEEKLY 검증 로직 실행
        matchingMissionResultService.validateMissionType1(userId, matchingId, missionId, missionScore, limitAmount);

        return ResponseEntity.ok("success validate codef_weekly mission");
    }

    @PutMapping("missions/validate/2")
    public ResponseEntity<?> validatemissionType2(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                  @RequestBody MatchingMissionUpdateRequest request) {
        //1. 유저아이디 추출
        Long userId = userDetails.getId();

        //2. 매칭, 미션, 제한 금액 추출
        Long matchingId = matchingService.getProgressMatchingIdByUserId(userId);
        if (matchingId == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "matching progress not found"));
        }
        int missionId = request.getMissionId();
        int limitAmount = missionService.getMissionLimitAmount(missionId);
        int missionScore = missionService.getMissionScore(missionId);

        //3.CODEF_DAILY 검증 로직 실행
        matchingMissionResultService.validateMissionType2(userId, matchingId, missionId, missionScore, limitAmount);

        return ResponseEntity.ok("success validate codef_daily mission");
    }

    @PutMapping("/missions/validate/3")
    public ResponseEntity<?> validateMissionType3(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody MatchingMissionUpdateRequest request) {

        //1. 유저아이디 추출
        Long userId = userDetails.getId();

        //2. 매칭, 미션아이디, 미션 점수 추출
        Long matchingId = matchingService.getProgressMatchingIdByUserId(userId);
        if (matchingId == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "matching progress not found"));
        }
        int missionId = request.getMissionId();
        Integer score = missionService.getMissionScore(missionId);
        if (score == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "mission score not found"));
        }

        matchingMissionResultService.updateMatchingMissionResult(
                userId, matchingId, missionId, score
        );

        List<MissionProgressDTO> progressList =
                matchingMissionResultService.getAllMissionProgressDTO(userId, matchingId);

        int updateScore = scoreService.getScoreByUserIdAndRoundNumber(userId, roundInfoService.getRoundNumber()) + score;

        return ResponseEntity.ok(
                MissionProgressListResponseDTO.builder()
                        .message("Mission progress updated")
                        .missionProgressDTOList(progressList)
                        .resultScore(updateScore)
                        .build()
        );
    }

    @PutMapping("missions/validate/4")
    public ResponseEntity<?> validatemissionType4(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                  @RequestBody MatchingMissionQuizUpdateDTO request) {
        //1-1. 유저아이디 추출
        Long userId = userDetails.getId();

        //1-2.유저가 얻은 점수 추출
        int score = request.getScore();

        //2. 매칭, 미션아이디 추출
        Long matchingId = matchingService.getProgressMatchingIdByUserId(userId);
        if (matchingId == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "matching progress not found"));
        }
        int missionId = request.getMissionId();

        //3.점수 업데이트
        matchingMissionResultService.updateMatchingMissionResult(
                userId, matchingId, missionId, score
        );

        return ResponseEntity.ok(Map.of(
                        "message", "퀴즈 미션 채점 완료",
                        "score", score)
        );
    }


}
