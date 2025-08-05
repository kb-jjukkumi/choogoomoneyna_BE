package com.choogoomoneyna.choogoomoneyna_be.report.controller.test;

import com.choogoomoneyna.choogoomoneyna_be.auth.jwt.CustomUserDetails;
import com.choogoomoneyna.choogoomoneyna_be.report.dto.response.ReportResponseDTO;
import com.choogoomoneyna.choogoomoneyna_be.report.service.ReportControlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test/report")
public class ReportTestController {

    private final ReportControlService reportControlService;

    @PostMapping("/generate")
    public ResponseEntity<ReportResponseDTO> generateReport(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getId();

        ReportResponseDTO dto = reportControlService.sendToReport(userId);
        return ResponseEntity.ok(dto);
    }
}
