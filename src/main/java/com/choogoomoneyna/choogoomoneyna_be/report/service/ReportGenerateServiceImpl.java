package com.choogoomoneyna.choogoomoneyna_be.report.service;

import com.choogoomoneyna.choogoomoneyna_be.account.codef.dto.AccountResponseDto;
import com.choogoomoneyna.choogoomoneyna_be.account.db.dto.TransactionItemDto;
import com.choogoomoneyna.choogoomoneyna_be.account.db.service.AccountDbService;
import com.choogoomoneyna.choogoomoneyna_be.report.dto.response.GptResponseDTO;
import com.choogoomoneyna.choogoomoneyna_be.report.vo.ReportVO;
import com.choogoomoneyna.choogoomoneyna_be.survey.service.SurveyResponseService;
import com.choogoomoneyna.choogoomoneyna_be.survey.service.SurveyWeightCalculator;
import com.choogoomoneyna.choogoomoneyna_be.user.enums.ChoogooMi;
import com.choogoomoneyna.choogoomoneyna_be.user.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportGenerateServiceImpl implements ReportGenerateService {

    private final ReportService reportService;
    private final SurveyResponseService surveyResponseService;
    private final UserService userService;
    private final AccountDbService accountDbService;
    private final AiClient aiClient;

    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 변환용

    private static final Map<ChoogooMi, String> DISPLAY_NAME_MAP = Map.of(
            ChoogooMi.A, "지출제로형",
            ChoogooMi.B, "합리소비형",
            ChoogooMi.C, "저축실천형",
            ChoogooMi.D, "투자도전형",
            ChoogooMi.E, "금융탐구형"
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
                    "actionItems", response.getActionItems(),
                    "totalSpent", response.getTotalSpent(),
                    "categorySpent",response.getCategorySpent()
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
                - 성향은 5가지 유형 중 하나입니다 (지출제로형, 합리소비형, 저축실천형, 투자도전형, 금융탐구형). 각 유형에 대한 설명은 다음과 같습니다.
                    지출제로형 - “1원도 아끼는 생활을 실천하는 절약왕”
                    합리소비형 - “소소한 행복은 지키되, 낭비는 줄이는 실속파”
                    저축실천형 - “비상금, 내 집 마련 등 뚜렷한 목표 저축을 실천하는 마스터”
                    투자도전형 - “작은 돈으로도 투자에 도전하고 배우는 재테크 입문러”
                    금융탐구형 - “궁금한 건 바로 검색하고 실천하는 금융 루틴러”
                - 계좌 정보는 잔액, 월별 입출금 패턴, 고정 지출, 저축 비율 등의 정보로 구성되어 있습니다.
                - 다른 텍스트 추가하지 말고 응답은 무조건 아래 JSON 구조 대로만 해, 코드블럭 없이, 마크다운 없이 순수 JSON만 보내줘.
                
                JSON 구조:
                {
                  "summary": "한 문장 요약",
                  "advice": "사용자에게 전달할 구체적인 재무 조언",
                  "recommend": "사용자의 생활과 가장 비슷한 추구미, 추구미 유형에 대한 설명은 빼고 유형 이름만 작성",
                  "action_items": [
                    "첫 번째 행동 권장사항",
                    "두 번째 행동 권장사항",
                    "세 번째 행동 권장사항"
                  ],
                   "totalSpent": 총 지출 금액 합계(앞으로 나오는 모든 숫자에서 ,없이 숫자만 출력),
                   "categorySpent": {
                     "식비": {"amount": 식비 카테고리의 총 금액, "ratio": 전체 중에서 차지하는 비율},
                     "교통비": {"amount": 교통비 카테고리의 총 금액, "ratio": 전체 중에서 차지하는 비율},
                     "쇼핑": {"amount": 쇼핑 카테고리의 총 금액, "ratio": 전체 중에서 차지하는 비율},
                     "기타": {"amount": 기타 카테고리의 총 금액, "ratio": 전체 중에서 차지하는 비율},
                   }
                }

                예시:
                {
                  "summary": "지출을 줄이고 자동 저축을 늘리는 것이 중요합니다.",
                  "advice": "매달 지출 내역을 점검하고, 고정 지출을 줄이는 방법을 찾아보세요.",
                  "recommend": "지출제로형"
                  "action_items": [
                    "정기 지출 항목 재검토",
                    "비상금 자동이체 설정",
                    "식비 항목에서 지출 비율 줄이기"
                  ],
                   "totalSpent": "2100000",
                   "categorySpent": {
                     "식비": { "amount": "900000", "ratio": "12.0" },
                     "교통비": { "amount": "900000", "ratio": "12.0" },
                     "쇼핑": { "amount": "900000", "ratio": "12.0" },
                     "기타": { "amount": "900000", "ratio": "12.0" }
                   }
                }
                - 위의 4가지 카테고리만으로 분석해서 카테고리 결과에 맞게 amount, ratio 작성해주세요.

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

        sb.append("""
                아래는 사용자의 최근 한 달간 계좌 거래내역입니다. 소비 습관, 고정 지출, 저축 패턴 등을 분석하여, 설문 점수와 함께 사용자에게 가장 적절한 재무 조언과 추천 유형을 제시해주세요.
                또한, 각 거래내역을 4가지 카테고리 식비, 교통비, 쇼핑, 기타로 분류해주고 각 분류의 금액과 비율을 계산해주세요.
                """);
        // TODO: 계좌 정보 연동시 이곳에 추가
        // sb.append("계좌 정보: ...\n");
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(30); //한달간의 거래내역 데이터 사용
        String start = startDate.toString();
        String end = endDate.toString();

        //사용자의 등록된 계좌내역 가져오기
        List<AccountResponseDto> accountList = accountDbService.getAllAccounts(userId);
        for(AccountResponseDto account : accountList) {
            //계좌별 최근 한 달간 거래내역 조회
            List<TransactionItemDto> transactions = accountDbService.getMonthlyTransactions(account.getAccountNum(),start, end);
            if(!transactions.isEmpty()) {
                sb.append("계좌번호: ").append(account.getAccountNum()).append("\n");

                //거래내역 내용 추가
                for (TransactionItemDto t : transactions) {
                    String date = t.getTrTime().split("T")[0]; // "2025-07-28"
                    String type = t.getTransactionType();       // "Input" or "Output"
                    int amount = type.equalsIgnoreCase("Input") ? t.getTrAccountIn() : t.getTrAccountOut();
                    String amountStr = (type.equalsIgnoreCase("Input") ? "+" : "-") + String.format("%,d", amount) + "원";

                    // 거래 주요 정보(desc3) 추가
                    String desc = String.join(" / ", Optional.ofNullable(t.getTrDesc3()).orElse(""));

                    String transactionLine = date + " | " + (type.equals("Input") ? "입금 " : "출금 ")
                            + amountStr + " | " + desc;

                    sb.append(transactionLine).append("\n");
                    log.info("🧾 거래내역 로그: {}", transactionLine);
                }

                sb.append("\n");
            }
        }


        System.out.println(sb);
        System.out.println(scores);

        return sb.toString();
    }
}
