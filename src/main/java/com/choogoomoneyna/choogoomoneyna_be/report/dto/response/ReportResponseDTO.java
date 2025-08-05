package com.choogoomoneyna.choogoomoneyna_be.report.dto.response;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportResponseDTO {
    private Date regDate;
    private String summary;            // 한 문장 요약
    private String advice;             // 구체적인 재무 조언
    private String recommend;          // 추천되는 추구미 유형
    private List<String> actionItems;  // 행동 권장 사항 리스트

}
