package com.choogoomoneyna.choogoomoneyna_be.survey.controller;

import com.choogoomoneyna.choogoomoneyna_be.auth.jwt.CustomUserDetails;
import com.choogoomoneyna.choogoomoneyna_be.survey.dto.request.SurveyRequestDTO;
import com.choogoomoneyna.choogoomoneyna_be.survey.service.SurveyCalculatorService;
import com.choogoomoneyna.choogoomoneyna_be.survey.service.SurveyConverter;
import com.choogoomoneyna.choogoomoneyna_be.survey.service.SurveyResponseService;
import com.choogoomoneyna.choogoomoneyna_be.user.enums.ChoogooMi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/survey")
public class SurveyResponseController {

    private final SurveyResponseService surveyResponseService;
    private final SurveyCalculatorService surveyCalculatorService;

    @PostMapping("/submit")
    ResponseEntity<Map<String, String>> submitSurveyResponse(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody SurveyRequestDTO surveyRequestDTO) {
        Long userId = userDetails.getId();
        if (userId == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "user id is null"));
        }

        surveyResponseService.insertBatchSurveyResponse(
                SurveyConverter.toSurveyResponseVOList(userId, surveyRequestDTO)
        );

        return ResponseEntity.ok(Map.of("message", "Successfully saved"));
    }

    @GetMapping("/recommend")
    ResponseEntity<?> recommendChoogooMi(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getId();
        if (userId == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "user id is null"));
        }

        ChoogooMi recommendedChoogooMi = surveyCalculatorService.recommendChoogooMi(userId);
        return ResponseEntity.ok(recommendedChoogooMi);
    }
}
