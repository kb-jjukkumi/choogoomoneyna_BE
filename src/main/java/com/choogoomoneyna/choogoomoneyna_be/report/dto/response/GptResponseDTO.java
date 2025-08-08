package com.choogoomoneyna.choogoomoneyna_be.report.dto.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GptResponseDTO {
    private String summary;            // 한 문장 요약
    private String advice;             // 구체적인 재무 조언
    private String recommend;          // 추천되는 추구미 유형

    @JsonAlias({"action_items", "actionItems"})
    private List<String> actionItems;  // 행동 권장 사항 리스트

    private Long totalSpent;           //총 지출 금액

    @JsonProperty("categorySpent")
    private Map<String, CategoryStat> categorySpent;

    @Getter@Setter@AllArgsConstructor@NoArgsConstructor
    public static class CategoryStat {
        private Long amount;           // 카테고리 금액 합,
        private Double ratio;          // 카테고리 비율
    }
}
