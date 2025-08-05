package com.choogoomoneyna.choogoomoneyna_be.report.service;

import com.choogoomoneyna.choogoomoneyna_be.report.vo.ReportVO;

import java.util.List;

public interface ReportService {

    void saveReport(ReportVO reportVO);

    List<ReportVO> findAllByUserId(Long userId);

    ReportVO findTopByUserIdOrderByRegDateDesc(Long userId);
}
