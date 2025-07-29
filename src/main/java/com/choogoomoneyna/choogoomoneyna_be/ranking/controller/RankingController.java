package com.choogoomoneyna.choogoomoneyna_be.ranking.controller;

import com.choogoomoneyna.choogoomoneyna_be.auth.jwt.CustomUserDetails;
import com.choogoomoneyna.choogoomoneyna_be.ranking.dto.response.RankingHistoryDTO;
import com.choogoomoneyna.choogoomoneyna_be.ranking.dto.response.RankingResponseDTO;
import com.choogoomoneyna.choogoomoneyna_be.ranking.service.RankingConverter;
import com.choogoomoneyna.choogoomoneyna_be.ranking.service.RankingService;
import com.choogoomoneyna.choogoomoneyna_be.ranking.service.RankingUpdateServiceImpl;
import com.choogoomoneyna.choogoomoneyna_be.ranking.service.RankingUserService;
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

    @PutMapping("/update")
    public ResponseEntity<?> updateRanking() {
        rankingUpdateService.updateRanking();
        return ResponseEntity.ok("Ranking has been updated");
    }

    @GetMapping("/list/top50")
    public ResponseEntity<?> getRankingList() {
        List<RankingResponseDTO> rankingList = rankingUserService.findTop50LatestRankingUserPerUser()
                .stream()
                .map(RankingConverter::toRankingResponseDTO)
                .toList();

        return ResponseEntity.ok(rankingList);
    }

    @GetMapping("/list/previous/top3")
    public ResponseEntity<?> getLastRankingListTop3() {
        List<RankingResponseDTO> rankingList = rankingUserService.findTop3BySecondLatestRankingByRegDate()
                .stream()
                .map(RankingConverter::toRankingResponseDTO)
                .toList();

        return ResponseEntity.ok(rankingList);
    }

    @GetMapping("/history")
    public ResponseEntity<?> getRankingHistory(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getId();

        List<RankingHistoryDTO> rankingHistoryList = rankingService.findAllRankingByUserId(userId)
                .stream()
                .map(RankingConverter::toRankingHistoryDTO)
                .toList();

        return ResponseEntity.ok(rankingHistoryList);
    }
}
