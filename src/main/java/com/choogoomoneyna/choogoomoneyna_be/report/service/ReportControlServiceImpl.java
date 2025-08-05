package com.choogoomoneyna.choogoomoneyna_be.report.service;

import com.choogoomoneyna.choogoomoneyna_be.report.dto.response.ReportResponseDTO;
import com.choogoomoneyna.choogoomoneyna_be.report.vo.ReportVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ReportControlServiceImpl implements ReportControlService {

    private final ReportService reportService;
    private final ReportGenerateService reportGenerateService;

    @Override
    public ReportResponseDTO sendToReport(Long userId) {
        System.out.println("userId: " + userId);
        try {
            ReportVO latestReport = reportService.findTopByUserIdOrderByRegDateDesc(userId);
            long periodMillis = TimeUnit.DAYS.toMillis(2);  // 2일 제한

            // 만료되었으면 새로 뽑기
            if (latestReport == null || isExpired(latestReport.getRegDate(), periodMillis)) {
                latestReport = reportGenerateService.generateReport(userId);
            }

            return ReportConverter.toResponseDTO(latestReport);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }

    }

    private boolean isExpired(Date regDate, long periodMillis) {
        if (regDate == null) return true; // regDate 없으면 무조건 새로 생성
        long now = System.currentTimeMillis();
        long regTime = regDate.getTime();
        return (now - regTime) > periodMillis;
    }
}
