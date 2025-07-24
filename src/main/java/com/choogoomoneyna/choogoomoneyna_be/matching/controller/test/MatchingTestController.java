package com.choogoomoneyna.choogoomoneyna_be.matching.controller.test;

import com.choogoomoneyna.choogoomoneyna_be.matching.service.MatchingService;
import com.choogoomoneyna.choogoomoneyna_be.matching.service.MatchingMissionResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test/matching")
@RequiredArgsConstructor
public class MatchingTestController {

    private final MatchingService matchingService;
    private final MatchingMissionResultService matchingMissionResultService;

    /**
     * 모든 사용자에 대해 매칭 시작 (테스트용)
     * <p>확인 완료</p>
     */
    @PostMapping("/start-all")
    public ResponseEntity<String> startAllMatching() {
        System.out.println("start all matching");
        matchingService.startAllMatching();
        return ResponseEntity.ok("Matching started for all users (test)");
    }

    /**
     * 특정 유저에 대해 매칭 시작 (테스트용)
     */
    @PostMapping("/start/{userId}")
    public ResponseEntity<String> startMatchingForUser(@PathVariable Long userId) {
        matchingService.startMatching(userId);
        return ResponseEntity.ok("Matching started for user " + userId + " (test)");
    }

    /**
     * 매칭 결과 점수 조회 (테스트용)
     */
    @GetMapping("/score/{userId}/{matchingId}")
    public ResponseEntity<Integer> getAllScore(@PathVariable Long userId, @PathVariable Long matchingId) {
        int score = matchingMissionResultService.getAllScoreByUserIdAndMatchingId(userId, matchingId);
        System.out.println("score = " + score);
        return ResponseEntity.ok(score);
    }

    /**
     * 테스트용으로 매칭 상태 변경
     */
    @PostMapping("/update-status/{matchingId}")
    public ResponseEntity<String> updateMatchingStatus(@PathVariable Long matchingId,
                                                       @RequestParam String status) {
        matchingService.updateMatchingStatus(matchingId, status);
        return ResponseEntity.ok("Matching status updated (test)");
    }

    /**
     * 테스트용 매칭 상태 조회
     */
    @GetMapping("/status/{matchingId}")
    public ResponseEntity<String> getMatchingStatus(@PathVariable Long matchingId) {
        String status = matchingService.findMatchingStatus(matchingId);
        return ResponseEntity.ok(status);
    }
}
