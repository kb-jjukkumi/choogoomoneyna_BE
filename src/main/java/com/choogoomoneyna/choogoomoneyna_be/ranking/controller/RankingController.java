package com.choogoomoneyna.choogoomoneyna_be.ranking.controller;

import com.choogoomoneyna.choogoomoneyna_be.auth.jwt.CustomUserDetails;
import com.choogoomoneyna.choogoomoneyna_be.matching.service.RoundInfoService;
import com.choogoomoneyna.choogoomoneyna_be.ranking.dto.response.RankingChangeResponseDTO;
import com.choogoomoneyna.choogoomoneyna_be.ranking.dto.response.RankingHistoryDTO;
import com.choogoomoneyna.choogoomoneyna_be.ranking.dto.response.RankingResponseDTO;
import com.choogoomoneyna.choogoomoneyna_be.ranking.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ranking")
public class RankingController {

    private final RankingUserService rankingUserService;
    private final RankingUpdateServiceImpl rankingUpdateService;
    private final RankingService rankingService;
    private final RoundInfoService roundInfoService;
    private final RankingUserChangeService rankingUserChangeService;
    private final RankingHistoryService rankingHistoryService;

    @PutMapping("/update")
    public ResponseEntity<?> updateRanking() {
        rankingUpdateService.updateRanking();
        return ResponseEntity.ok("Ranking has been updated");
    }

    @GetMapping("/list/top50")
    public ResponseEntity<?> getRankingList() {
        List<RankingChangeResponseDTO> rankingList =
                rankingUserChangeService.findTopNRankingUserChangeByRoundNumber(roundInfoService.getRoundNumber(), 50)
                .stream()
                .map(RankingConverter::toRankingChangeResponseDTO)
                .toList();

        return ResponseEntity.ok(rankingList);
    }

    @GetMapping("/list/previous/top3")
    public ResponseEntity<?> getLastRankingListTop3() {
        List<RankingResponseDTO> rankingList =
                rankingUserService.findTopNRankingUserByRoundNumber(roundInfoService.getRoundNumber()-1, 3)
                .stream()
                .map(RankingConverter::toRankingResponseDTO)
                .toList();

        return ResponseEntity.ok(rankingList);
    }

    @GetMapping("/history")
    public ResponseEntity<?> getRankingHistory(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getId();

        List<RankingHistoryDTO> rankingHistoryList = rankingHistoryService.findAllRankingHistoryByUserId(userId)
                .stream()
                .map(RankingConverter::toRankingHistoryDTO)
                .toList();

        return ResponseEntity.ok(rankingHistoryList);
    }
}
