package com.choogoomoneyna.choogoomoneyna_be.report.controller;

import com.choogoomoneyna.choogoomoneyna_be.auth.jwt.CustomUserDetails;
import com.choogoomoneyna.choogoomoneyna_be.report.dto.response.ReportResponseDTO;
import com.choogoomoneyna.choogoomoneyna_be.report.service.ReportControlService;
import com.choogoomoneyna.choogoomoneyna_be.report.service.ReportConverter;
import com.choogoomoneyna.choogoomoneyna_be.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/report")
public class ReportController {

    private final ReportControlService reportControlService;
    private final ReportService reportService;

    @PostMapping("/create")
    public ResponseEntity<ReportResponseDTO> createReport(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getId();

        ReportResponseDTO dto = reportControlService.sendToReport(userId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/list")
    public ResponseEntity<?> getReportList(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getId();

        List<ReportResponseDTO> responseDTOs = reportService.findAllByUserId(userId)
                .stream()
                .map(ReportConverter::toResponseDTO)
                .toList();

        return ResponseEntity.ok(responseDTOs);
    }
}
