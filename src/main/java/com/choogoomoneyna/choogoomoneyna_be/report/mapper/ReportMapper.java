package com.choogoomoneyna.choogoomoneyna_be.report.mapper;

import com.choogoomoneyna.choogoomoneyna_be.report.vo.ReportVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReportMapper {

    void insertReport(ReportVO reportVO);

    List<ReportVO> findAllByUserId(Long userId);

    ReportVO findTopByUserIdOrderByRegDateDesc(Long userId);
}
