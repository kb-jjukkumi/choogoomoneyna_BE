package com.choogoomoneyna.choogoomoneyna_be.mission.service;

import com.choogoomoneyna.choogoomoneyna_be.mission.mapper.MissionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MissionServiceImpl implements MissionService {

    private final MissionMapper missionMapper;

    @Override
    public Integer getMissionScore(Integer missionId) {
        return missionMapper.getMissionScore(missionId);
    }

    @Override
    public Integer getMissionLimitAmount(Integer missionId) {
        return missionMapper.getMissionLimitAmount(missionId);
    }

    @Override
    public String getMissionType(Integer missionId) {
        return missionMapper.getMissionType(missionId);
    }
}
