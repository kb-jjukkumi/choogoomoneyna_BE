package com.choogoomoneyna.choogoomoneyna_be.report.service;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import com.choogoomoneyna.choogoomoneyna_be.user.enums.ChoogooMi;

public class SurveyWeight {

    // 질문별 선택지별 가중치 매핑
    private final Map<Integer, Map<Integer, Map<ChoogooMi, Integer>>> weightMap = new HashMap<>();

    public SurveyWeight() {
        //타입1(5지선다) 1번
        Map<Integer, Map<ChoogooMi, Integer>> q1 = new HashMap<>();
        q1.put(1, Map.of(
                ChoogooMi.A, 7,
                ChoogooMi.B, 5,
                ChoogooMi.C, 3,
                ChoogooMi.D, 0,
                ChoogooMi.E, 9
        ));
        q1.put(2, Map.of(
                ChoogooMi.A, 9,
                ChoogooMi.B, 7,
                ChoogooMi.C, 5,
                ChoogooMi.D, 0,
                ChoogooMi.E, 1
        ));
        q1.put(3, Map.of(
                ChoogooMi.A, 0,
                ChoogooMi.B, 3,
                ChoogooMi.C, 1,
                ChoogooMi.D, 9,
                ChoogooMi.E, 5
        ));
        q1.put(4, Map.of(
                ChoogooMi.A, 1,
                ChoogooMi.B, 3,
                ChoogooMi.C, 0,
                ChoogooMi.D, 9,
                ChoogooMi.E, 5
        ));
        q1.put(5, Map.of(
                ChoogooMi.A, 3,
                ChoogooMi.B, 3,
                ChoogooMi.C, 3,
                ChoogooMi.D, 3,
                ChoogooMi.E, 3
        ));
        weightMap.put(1, q1);

        //타입1 2번
        Map<Integer, Map<ChoogooMi, Integer>> q2 = new HashMap<>();
        q2.put(1, Map.of(
                ChoogooMi.A, 9,
                ChoogooMi.B, 5,
                ChoogooMi.C, 3,
                ChoogooMi.D, 0,
                ChoogooMi.E, 1
        ));
        q2.put(2, Map.of(
                ChoogooMi.A, 9,
                ChoogooMi.B, 5,
                ChoogooMi.C, 3,
                ChoogooMi.D, 0,
                ChoogooMi.E, 1
        ));
        q2.put(3, Map.of(
                ChoogooMi.A, 5,
                ChoogooMi.B, 9,
                ChoogooMi.C, 3,
                ChoogooMi.D, 1,
                ChoogooMi.E, 0
        ));
        q2.put(4, Map.of(
                ChoogooMi.A, 0,
                ChoogooMi.B, 3,
                ChoogooMi.C, 5,
                ChoogooMi.D, 9,
                ChoogooMi.E, 1
        ));
        q2.put(5, Map.of(
                ChoogooMi.A, 0,
                ChoogooMi.B, 3,
                ChoogooMi.C, 5,
                ChoogooMi.D, 9,
                ChoogooMi.E, 1
        ));
        weightMap.put(2, q2);

        //타입1-3번
        Map<Integer, Map<ChoogooMi, Integer>> q3 = new HashMap<>();
        q3.put(1, Map.of(
                ChoogooMi.A, 1,
                ChoogooMi.B, 5,
                ChoogooMi.C, 0,
                ChoogooMi.D, 7,
                ChoogooMi.E, 3
        ));
        q3.put(2, Map.of(
                ChoogooMi.A, 0,
                ChoogooMi.B, 5,
                ChoogooMi.C, 1,
                ChoogooMi.D, 7,
                ChoogooMi.E, 3
        ));
        q3.put(3, Map.of(
                ChoogooMi.A, 1,
                ChoogooMi.B, 5,
                ChoogooMi.C, 3,
                ChoogooMi.D, 2,
                ChoogooMi.E, 7
        ));
        q3.put(4, Map.of(
                ChoogooMi.A, 0,
                ChoogooMi.B, 3,
                ChoogooMi.C, 5,
                ChoogooMi.D, 1,
                ChoogooMi.E, 7
        ));
        q3.put(5, Map.of(
                ChoogooMi.A, 0,
                ChoogooMi.B, 1,
                ChoogooMi.C, 9,
                ChoogooMi.D, 3,
                ChoogooMi.E, 5
        ));
        weightMap.put(3, q3);

        //타입1-4번
        Map<Integer, Map<ChoogooMi, Integer>> q4 = new HashMap<>();
        q4.put(1, Map.of(
                ChoogooMi.A, 9,
                ChoogooMi.B, 1,
                ChoogooMi.C, 3,
                ChoogooMi.D, 0,
                ChoogooMi.E, 1
        ));
        q4.put(2, Map.of(
                ChoogooMi.A, 3,
                ChoogooMi.B, 9,
                ChoogooMi.C, 3,
                ChoogooMi.D, 3,
                ChoogooMi.E, 1
        ));
        q4.put(3, Map.of(
                ChoogooMi.A, 1,
                ChoogooMi.B, 9,
                ChoogooMi.C, 1,
                ChoogooMi.D, 2,
                ChoogooMi.E, 2
        ));
        q4.put(4, Map.of(
                ChoogooMi.A, 0,
                ChoogooMi.B, 0,
                ChoogooMi.C, 0,
                ChoogooMi.D, 1,
                ChoogooMi.E, 9
        ));
        q4.put(5, Map.of(
                ChoogooMi.A, 0,
                ChoogooMi.B, 0,
                ChoogooMi.C, 1,
                ChoogooMi.D, 1,
                ChoogooMi.E, 9
        ));
        weightMap.put(4, q4);

        for(int i=5; i<=9; i++) {
            Map<Integer, Map<ChoogooMi, Integer>> q = new HashMap<>();
            q.put(1, Map.of(
                    ChoogooMi.A, 9,
                    ChoogooMi.B, 7,
                    ChoogooMi.C, 5,
                    ChoogooMi.D, 1,
                    ChoogooMi.E, 1
            ));
            q.put(2, Map.of(
                    ChoogooMi.A, 7,
                    ChoogooMi.B, 9,
                    ChoogooMi.C, 3,
                    ChoogooMi.D, 1,
                    ChoogooMi.E, 1
            ));
            q.put(3, Map.of(
                    ChoogooMi.A, 5,
                    ChoogooMi.B, 9,
                    ChoogooMi.C, 0,
                    ChoogooMi.D, 1,
                    ChoogooMi.E, 1
            ));
            q.put(4, Map.of(
                    ChoogooMi.A, 0,
                    ChoogooMi.B, 1,
                    ChoogooMi.C, 0,
                    ChoogooMi.D, 0,
                    ChoogooMi.E, 3
            ));
            weightMap.put(i, q);
        }

        for(int i=10; i<=14; i++) {
            Map<Integer, Map<ChoogooMi, Integer>> q = new HashMap<>();
            q.put(1, Map.of(
                    ChoogooMi.A, 9,
                    ChoogooMi.B, 7,
                    ChoogooMi.C, 5,
                    ChoogooMi.D, 1,
                    ChoogooMi.E, 1
            ));
            q.put(2, Map.of(
                    ChoogooMi.A, 0,
                    ChoogooMi.B, 1,
                    ChoogooMi.C, 1,
                    ChoogooMi.D, 3,
                    ChoogooMi.E, 3
            ));
            weightMap.put(i, q);
        }
    }

    // 사용자 응답 기반 점수 계산
    public Map<ChoogooMi, Integer> calculateScore(Map<Integer, Integer> responses) {
        Map<ChoogooMi, Integer> scores = new EnumMap<>(ChoogooMi.class);
        for (ChoogooMi type : ChoogooMi.values()) {
            scores.put(type, 0);
        }

        for (Map.Entry<Integer, Integer> entry : responses.entrySet()) {
            int questionId = entry.getKey();
            int optionValue = entry.getValue();
            Map<ChoogooMi, Integer> weights = weightMap.getOrDefault(questionId, Collections.emptyMap()).get(optionValue);
            if (weights != null) {
                for (Map.Entry<ChoogooMi, Integer> w : weights.entrySet()) {
                    scores.put(w.getKey(), scores.get(w.getKey()) + w.getValue());
                }
            }
        }

        return scores;
    }

    // 최고 유형 리턴
    public ChoogooMi getTopType(Map<Integer, Integer> responses) {
        Map<ChoogooMi, Integer> scores = calculateScore(responses);
        return scores.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

}
