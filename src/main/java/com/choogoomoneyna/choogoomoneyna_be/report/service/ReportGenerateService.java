package com.choogoomoneyna.choogoomoneyna_be.report.service;

import com.choogoomoneyna.choogoomoneyna_be.report.vo.ReportVO;

public interface ReportGenerateService {

    /**
     * 특정 사용자의 데이터와 행동을 기반으로 재무 보고서를 생성합니다.
     *
     * @param userId 보고서를 생성할 사용자의 고유 식별자
     * @return 제목, 내용, 등록일과 같은 생성된 보고서 정보가 포함된 {@link ReportVO} 객체
     */
    ReportVO generateReport(Long userId);
}
