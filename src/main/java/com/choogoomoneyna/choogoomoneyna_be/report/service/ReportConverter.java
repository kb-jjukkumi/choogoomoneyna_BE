package com.choogoomoneyna.choogoomoneyna_be.report.service;

import com.choogoomoneyna.choogoomoneyna_be.report.dto.response.GptResponseDTO;
import com.choogoomoneyna.choogoomoneyna_be.report.dto.response.ReportResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.choogoomoneyna.choogoomoneyna_be.report.vo.ReportVO;

public class ReportConverter {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static ReportResponseDTO toResponseDTO(ReportVO reportVO) {
        try {
            GptResponseDTO gptContent = objectMapper.readValue(reportVO.getContent(), GptResponseDTO.class);

            return ReportResponseDTO.builder()
                    .regDate(reportVO.getRegDate())
                    .summary(gptContent.getSummary())
                    .advice(gptContent.getAdvice())
                    .recommend(gptContent.getRecommend())
                    .actionItems(gptContent.getActionItems())
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("DB에서 Report content를 꺼내 역직렬화하는데 실패했습니다.", e);
        }
    }
}

