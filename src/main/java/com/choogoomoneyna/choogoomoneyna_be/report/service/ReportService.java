package com.choogoomoneyna.choogoomoneyna_be.report.service;

import com.choogoomoneyna.choogoomoneyna_be.report.vo.ReportVO;

import java.util.List;

public interface ReportService {

    /**
     * 주어진 신고 정보를 저장합니다.
     *
     * @param reportVO 사용자 ID, 제목, 내용, 등록일 등의 정보가 포함된 신고 데이터
     */
    void saveReport(ReportVO reportVO);

    /**
     * 주어진 사용자 ID와 관련된 모든 신고 목록을 조회합니다.
     *
     * @param userId 신고 목록을 조회할 사용자의 고유 식별자
     * @return 지정된 사용자의 모든 신고 정보가 포함된 ReportVO 객체 리스트
     */
    List<ReportVO> findAllByUserId(Long userId);

    /**
     * 주어진 사용자 ID와 관련된 가장 최근의 신고를 조회합니다.
     * 등록일 기준 내림차순으로 정렬됩니다.
     *
     * @param userId 최근 신고를 조회할 사용자의 고유 식별자
     * @return 가장 최근의 신고 정보가 담긴 ReportVO 객체,
     * 해당 사용자의 신고가 없는 경우 null 반환
     */
    ReportVO findTopByUserIdOrderByRegDateDesc(Long userId);
}