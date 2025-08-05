package com.choogoomoneyna.choogoomoneyna_be.report.service;

import com.choogoomoneyna.choogoomoneyna_be.report.dto.response.GptResponseDTO;
import com.choogoomoneyna.choogoomoneyna_be.report.vo.ReportVO;
import com.choogoomoneyna.choogoomoneyna_be.survey.service.SurveyResponseService;
import com.choogoomoneyna.choogoomoneyna_be.survey.service.SurveyWeightCalculator;
import com.choogoomoneyna.choogoomoneyna_be.user.enums.ChoogooMi;
import com.choogoomoneyna.choogoomoneyna_be.user.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ReportGenerateServiceImpl implements ReportGenerateService {

    private final ReportService reportService;
    private final SurveyResponseService surveyResponseService;
    private final UserService userService;
    private final AiClient aiClient;

    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 변환용

    private static final Map<ChoogooMi, String> DISPLAY_NAME_MAP = Map.of(
            ChoogooMi.A, "극한절약러 (Extreme Saver) - ‘1원도 아끼는 절약왕’",
            ChoogooMi.B, "생활절약형 (실속형 절약러) - ‘합리적 소비 실천가’",
            ChoogooMi.C, "목표 저축러 (저축 중심) - ‘저축마스터’",
            ChoogooMi.D, "투자 스타터 (투자 중심) - ‘성장헌터’",
            ChoogooMi.E, "프로준비러 (시드머니·정보 수집 중심) - 자산관리 입문자"
    );

    @Override
    public ReportVO generateReport(Long userId) {
        String prompt = generatePrompt(userId);

        GptResponseDTO response = aiClient.requestGpt(prompt);
        if (response == null) {
            throw new RuntimeException("GPT 오류");
        } else if (response.getSummary() == null) {
            throw new RuntimeException("GPT 비어있음");
        }

        String contentJson;
        try {
            contentJson = objectMapper.writeValueAsString(Map.of(
                    "summary", response.getSummary(),
                    "advice", response.getAdvice(),
                    "recommend", response.getRecommend(),
                    "actionItems", response.getActionItems()
            ));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("GPT 응답 직렬화 실패", e);
        }

        ReportVO report = ReportVO.builder()
                .regDate(new Date())
                .userId(userId)
                .title(response.getSummary())
                .content(contentJson)
                .build();

        reportService.saveReport(report);
        return report;
    }

    private String generatePrompt(Long userId) {
        Map<ChoogooMi, Integer> scores =
                SurveyWeightCalculator.calculateScore(surveyResponseService.findLatestSurveyResponseByUserId(userId));
        System.out.println("scores: " + scores);
        ChoogooMi currentType = userService.getChoogooMiByUserId(userId);

        StringBuilder sb = new StringBuilder();
        sb.append("""
                다음은 사용자의 자산정보와 성향 설문 결과입니다. 이를 바탕으로 사용자에게 적절한 재무 조언을 생성해 주세요.

                조건:
                - 성향은 5가지 유형 중 하나입니다 (극한절약러, 생활절약형, 목표 저축러, 투자 스타터, 프로준비러).
                - 계좌 정보는 잔액, 월별 입출금 패턴, 고정 지출, 저축 비율 등의 정보로 구성되어 있습니다.
                - 응답을 JSON 형식으로 해줘. 코드블럭 없이, 마크다운 없이 순수 JSON만 보내줘.
                

                JSON 구조:
                {
                  "summary": "한 문장 요약",
                  "advice": "사용자에게 전달할 구체적인 재무 조언",
                  "recommend": "사용자의 생활과 가장 비슷한 추구미",
                  "action_items": [
                    "첫 번째 행동 권장사항",
                    "두 번째 행동 권장사항"
                  ]
                }

                예시:
                {
                  "summary": "지출을 줄이고 자동 저축을 늘리는 것이 중요합니다.",
                  "advice": "매달 지출 내역을 점검하고, 고정 지출을 줄이는 방법을 찾아보세요.",
                  "recommend": "생활절약형",
                  "action_items": [
                    "정기 지출 항목 재검토",
                    "비상금 자동이체 설정"
                  ]
                }

                ===사용자의 현재 정보===
                """);

        sb.append("현재 추구미: ").append(DISPLAY_NAME_MAP.get(currentType)).append("\n\n");
        sb.append("설문 기반 점수:\n");

        scores.forEach((type, score) -> {
            if (type != ChoogooMi.O) {
                sb.append("- ")
                        .append(DISPLAY_NAME_MAP.get(type))
                        .append(": ")
                        .append(score)
                        .append("점\n");
            }
        });

        // TODO: 계좌 정보 연동시 이곳에 추가
        // sb.append("계좌 정보: ...\n");

        System.out.println(sb);
        System.out.println(scores);

        return sb.toString();
    }
}
