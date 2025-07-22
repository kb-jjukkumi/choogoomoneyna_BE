package com.choogoomoneyna.choogoomoneyna_be.matching.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Getter
public enum CommonMissionType {
    COMMON(Arrays.asList(1, 2, 3, 4));

    private final List<Integer> missionIds;

    CommonMissionType(List<Integer> missionIds) {
        this.missionIds = missionIds;
    }

    public int getRandomId() {
        return missionIds.get(new Random().nextInt(missionIds.size()));
    }
}

