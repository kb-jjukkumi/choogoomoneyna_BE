package com.choogoomoneyna.choogoomoneyna_be.user.enums;

import com.choogoomoneyna.choogoomoneyna_be.matching.enums.ChoogooMiMissionType;

public enum ChoogooMi {
    O,
    A,
    B,
    C,
    D,
    E;

    public ChoogooMiMissionType getMissionType() {
        switch (this) {
            case O: return ChoogooMiMissionType.O; // O 타입 추가
            case A: return ChoogooMiMissionType.A;
            case B: return ChoogooMiMissionType.B;
            case C: return ChoogooMiMissionType.C;
            case D: return ChoogooMiMissionType.D;
            case E: return ChoogooMiMissionType.E;
            default: throw new IllegalArgumentException("Unknown ChoogooMi: " + this);
        }
    }

}
