package com.choogoomoneyna.choogoomoneyna_be.report.service;

import com.choogoomoneyna.choogoomoneyna_be.report.mapper.ReportMapper;
import com.choogoomoneyna.choogoomoneyna_be.report.vo.ReportVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportMapper reportMapper;

    @Override
    public void saveReport(ReportVO reportVO) {
        reportMapper.insertReport(reportVO);
    }

    @Override
    public List<ReportVO> findAllByUserId(Long userId) {
        return reportMapper.findAllByUserId(userId);
    }

    @Override
    public ReportVO findTopByUserIdOrderByRegDateDesc(Long userId) {
        return reportMapper.findTopByUserIdOrderByRegDateDesc(userId);
    }
}
