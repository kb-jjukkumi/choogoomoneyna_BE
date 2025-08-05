package com.choogoomoneyna.choogoomoneyna_be.matching.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum ChoogooMiMissionType {
    O(null),
    A(Arrays.asList(101, 102, 103, 104)),
    B(Arrays.asList(201, 202, 203, 204)),
    C(Arrays.asList(301, 302, 303, 304)),
    D(Arrays.asList(401, 402, 403, 404, 405, 406, 407)),
    E(Arrays.asList(501, 502, 503, 504, 505, 506));

    private final List<Integer> missionIds;

    ChoogooMiMissionType(List<Integer> missionIds) {
        this.missionIds = missionIds;
    }

    // 랜덤 n개 미션을 중복없이 뽑아서 아이디 리스트로 반환 
    public List<Integer> getRandomIds(int count) {
        if (count > missionIds.size()) {
            throw new IllegalArgumentException("count가 미션 개수보다 클 수 없습니다.");
        }
        List<Integer> shuffled = new ArrayList<>(missionIds);
        Collections.shuffle(shuffled);
        return shuffled.subList(0, count);
    }
}

