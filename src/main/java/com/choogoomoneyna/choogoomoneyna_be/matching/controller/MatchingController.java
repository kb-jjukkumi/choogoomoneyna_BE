package com.choogoomoneyna.choogoomoneyna_be.matching.controller;

import com.choogoomoneyna.choogoomoneyna_be.matching.service.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/matching")
public class MatchingController {

    private final MatchingService matchingService;

    @PostMapping("/all-matching")
    public ResponseEntity<?> allMatching() {
        matchingService.startAllMatching();
        return ResponseEntity.ok(Map.of("message", "모든 유저가 매칭되었음"));
    }

}
