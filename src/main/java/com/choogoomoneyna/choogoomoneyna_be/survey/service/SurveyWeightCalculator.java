package com.choogoomoneyna.choogoomoneyna_be.survey.service;

import com.choogoomoneyna.choogoomoneyna_be.survey.vo.SurveyResponseVO;
import com.choogoomoneyna.choogoomoneyna_be.user.enums.ChoogooMi;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SurveyWeightCalculator {

    private static final Map<Integer, Map<Integer, Map<ChoogooMi, Integer>>> weightMap = new HashMap<>();

    static {
        initializeWeights();
    }

    private static void initializeWeights() {
        // 1번 문항
        addQuestionWeights(1, Map.of(
                1, createWeights(29, 21, 13, 0, 37),
                2, createWeights(45, 35, 25, 0, 0),
                3, createWeights(0, 19, 6, 56, 19),
                4, createWeights(6, 19, 0, 56, 19),
                5, createWeights(20, 20, 20, 20, 20)
        ));

        // 2번 문항
        addQuestionWeights(2, Map.of(
                1, createWeights(45, 25, 15, 0, 15),
                2, createWeights(45, 25, 15, 0, 15),
                3, createWeights(18, 56, 19, 6, 1),
                4, createWeights(0, 19, 31, 46, 4),
                5, createWeights(0, 19, 31, 50, 0)
        ));

        // 3번 문항
        addQuestionWeights(3, Map.of(
                1, createWeights(8, 42, 0, 48, 2),
                2, createWeights(0, 42, 8, 48, 2),
                3, createWeights(6, 31, 19, 13, 31),
                4, createWeights(0, 19, 31, 6, 44),
                5, createWeights(0, 6, 56, 19, 19)
        ));

        // 4번 문항
        addQuestionWeights(4, Map.of(
                1, createWeights(56, 6, 19, 0, 19),
                2, createWeights(25, 75, 0, 0, 0),
                3, createWeights(6, 56, 6, 13, 19),
                4, createWeights(0, 0, 0, 10, 90),
                5, createWeights(0, 0, 10, 10, 80)
        ));

        // 5~9번 문항
        for (int i = 5; i <= 9; i++) {
            addQuestionWeights(i, Map.of(
                    1, createWeights(45, 35, 15, 5, 0),
                    2, createWeights(35, 45, 15, 5, 0),
                    3, createWeights(25, 45, 0, 5, 25),
                    4, createWeights(0, 25, 0, 0, 75)
            ));
        }

        // 10~14번 문항
        for (int i = 10; i <= 14; i++) {
            addQuestionWeights(i, Map.of(
                    1, createWeights(45, 35, 15, 5, 0),
                    2, createWeights(0, 6, 6, 19, 69)
            ));
        }
    }


    private static void addQuestionWeights(int questionId, Map<Integer, Map<ChoogooMi, Integer>> options) {
        weightMap.put(questionId, options);
    }

    private static Map<ChoogooMi, Integer> createWeights(int a, int b, int c, int d, int e) {
        return Map.of(
                ChoogooMi.A, a,
                ChoogooMi.B, b,
                ChoogooMi.C, c,
                ChoogooMi.D, d,
                ChoogooMi.E, e
        );
    }

    /**
     * 제공된 설문 응답을 기반으로 각 추구미 유형별 점수를 계산하고 반환합니다.
     * 각 설문 문항과 응답 옵션별로 가중치 맵에 정의된 점수를 집계하여 계산합니다.
     *
     * @param responses 설문 문항 ID와 응답 옵션 ID가 포함된 설문 응답 목록
     * @return 추구미 유형을 키로 하고 해당 유형의 계산된 점수를 값으로 하는 맵
     */
    public static Map<ChoogooMi, Integer> calculateScore(List<SurveyResponseVO> responses) {
        Map<ChoogooMi, Integer> scores = new EnumMap<>(ChoogooMi.class);
        for (ChoogooMi type : ChoogooMi.values()) {
            scores.put(type, 0);
        }

        for (SurveyResponseVO response : responses) {
            int questionId = response.getSurveyQuestionId();
            int optionId = Integer.parseInt(response.getSurveyOptionId()); // 문자열을 정수로 변환

            Map<ChoogooMi, Integer> weights = weightMap
                    .getOrDefault(questionId, Collections.emptyMap())
                    .get(optionId);

            if (weights != null) {
                for (Map.Entry<ChoogooMi, Integer> w : weights.entrySet()) {
                    scores.put(w.getKey(), scores.get(w.getKey()) + w.getValue());
                }
            }
        }

        return scores;
    }

    public static ChoogooMi getTopType(List<SurveyResponseVO> responses) {
        return calculateScore(responses).entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }
}
