package com.choogoomoneyna.choogoomoneyna_be.ranking.controller;

import com.choogoomoneyna.choogoomoneyna_be.ranking.dto.response.RankingResponseDTO;
import com.choogoomoneyna.choogoomoneyna_be.ranking.service.RankingConverter;
import com.choogoomoneyna.choogoomoneyna_be.ranking.service.RankingUpdateServiceImpl;
import com.choogoomoneyna.choogoomoneyna_be.ranking.service.RankingUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @PutMapping("/update")
    public ResponseEntity<?> updateRanking() {
        rankingUpdateService.updateRanking();
        return ResponseEntity.ok("Ranking has been updated");
    }

    @GetMapping("/list")
    public ResponseEntity<?> getRankingList() {
        List<RankingResponseDTO> rankingList = rankingUserService.findLatestRankingUserPerUser()
                .stream()
                .map(RankingConverter::toRankingResponseDTO)
                .toList();

        return ResponseEntity.ok(rankingList);
    }

    @GetMapping("/previous/top3")
    public ResponseEntity<?> getLastRankingListTop3() {
        List<RankingResponseDTO> rankingList = rankingUserService.findTop3BySecondLatestRankingByRegDate()
                .stream()
                .map(RankingConverter::toRankingResponseDTO)
                .toList();

        return ResponseEntity.ok(rankingList);
    }
}
