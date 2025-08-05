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
        addQuestionWeights(1, Map.of(
                1, createWeights(7, 5, 3, 0, 9),
                2, createWeights(9, 7, 5, 0, 1),
                3, createWeights(0, 3, 1, 9, 5),
                4, createWeights(1, 3, 0, 9, 5),
                5, createWeights(3, 3, 3, 3, 3)
        ));

        addQuestionWeights(2, Map.of(
                1, createWeights(9, 5, 3, 0, 1),
                2, createWeights(9, 5, 3, 0, 1),
                3, createWeights(5, 9, 3, 1, 0),
                4, createWeights(0, 3, 5, 9, 1),
                5, createWeights(0, 3, 5, 9, 1)
        ));

        addQuestionWeights(3, Map.of(
                1, createWeights(1, 5, 0, 7, 3),
                2, createWeights(0, 5, 1, 7, 3),
                3, createWeights(1, 5, 3, 2, 7),
                4, createWeights(0, 3, 5, 1, 7),
                5, createWeights(0, 1, 9, 3, 5)
        ));

        addQuestionWeights(4, Map.of(
                1, createWeights(9, 1, 3, 0, 1),
                2, createWeights(3, 9, 3, 3, 1),
                3, createWeights(1, 9, 1, 2, 2),
                4, createWeights(0, 0, 0, 1, 9),
                5, createWeights(0, 0, 1, 1, 9)
        ));

        for (int i = 5; i <= 9; i++) {
            addQuestionWeights(i, Map.of(
                    1, createWeights(9, 7, 5, 1, 1),
                    2, createWeights(7, 9, 3, 1, 1),
                    3, createWeights(5, 9, 0, 1, 1),
                    4, createWeights(0, 1, 0, 0, 3)
            ));
        }

        for (int i = 10; i <= 14; i++) {
            addQuestionWeights(i, Map.of(
                    1, createWeights(9, 7, 5, 1, 1),
                    2, createWeights(0, 1, 1, 3, 3)
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
