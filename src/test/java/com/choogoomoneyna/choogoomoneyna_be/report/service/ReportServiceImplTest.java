package com.choogoomoneyna.choogoomoneyna_be.report.service;

import com.choogoomoneyna.choogoomoneyna_be.report.mapper.ReportMapper;
import com.choogoomoneyna.choogoomoneyna_be.report.vo.ReportVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReportServiceImplTest {

    @Mock
    private ReportMapper reportMapper;

    @InjectMocks
    private ReportServiceImpl reportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveReport() {
        // given
        ReportVO reportVO = ReportVO.builder()
                .userId(1L)
                .title("content title")
                .content("content")
                .build();

        // when
        reportService.saveReport(reportVO);

        // then
        verify(reportMapper, times(1)).insertReport(reportVO);
    }

    @Test
    void findAllByUserId() {
        // given
        Long userId = 1L;
        List<ReportVO> mockReports = Arrays.asList(
                new ReportVO(1L, userId, "title1", "content1", new Date()),
                new ReportVO(2L, userId, "title2", "content2", new Date())
        );
        when(reportMapper.findAllByUserId(userId)).thenReturn(mockReports);

        // when
        List<ReportVO> result = reportService.findAllByUserId(userId);

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("title1", result.get(0).getTitle());
        verify(reportMapper, times(1)).findAllByUserId(userId);
    }

    @Test
    void findTopByUserIdOrderByRegDateDesc() {
        // given
        Long userId = 1L;
        ReportVO mockReport = new ReportVO(1L, userId, "latestTitle", "latestContent", new Date());
        when(reportMapper.findTopByUserIdOrderByRegDateDesc(userId)).thenReturn(mockReport);

        // when
        ReportVO result = reportService.findTopByUserIdOrderByRegDateDesc(userId);

        // then
        assertNotNull(result);
        assertEquals("latestTitle", result.getTitle());
        verify(reportMapper, times(1)).findTopByUserIdOrderByRegDateDesc(userId);
    }
}