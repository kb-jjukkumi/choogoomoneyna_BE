package com.choogoomoneyna.choogoomoneyna_be.survey.controller;

import com.choogoomoneyna.choogoomoneyna_be.auth.jwt.CustomUserDetails;
import com.choogoomoneyna.choogoomoneyna_be.survey.dto.request.SurveyRequestDTO;
import com.choogoomoneyna.choogoomoneyna_be.survey.service.SurveyConverter;
import com.choogoomoneyna.choogoomoneyna_be.survey.service.SurveyResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/survey")
public class SurveyResponseController {

    private final SurveyResponseService surveyResponseService;

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
}
