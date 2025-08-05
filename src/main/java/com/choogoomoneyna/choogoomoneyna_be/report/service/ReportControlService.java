package com.choogoomoneyna.choogoomoneyna_be.report.service;

import com.choogoomoneyna.choogoomoneyna_be.report.dto.response.ReportResponseDTO;

public interface ReportControlService {

    /**
     * 주어진 사용자 ID에 대한 리포트를 조회하거나 생성합니다. 사용자의 가장 최근 리포트가
     * 만료 기간보다 오래되었거나 리포트가 없는 경우 새로운 리포트를 생성합니다.
     *
     * @param userId 리포트를 조회하거나 생성할 사용자의 고유 식별자
     * @return 등록일, 재무 조언, 추천사항, 행동 권장사항 등이 포함된 {@link ReportResponseDTO}
     */
    ReportResponseDTO sendToReport(Long userId);
}
