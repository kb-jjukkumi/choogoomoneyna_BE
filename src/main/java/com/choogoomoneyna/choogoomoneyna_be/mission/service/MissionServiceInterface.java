package com.choogoomoneyna.choogoomoneyna_be.mission.service;

import com.choogoomoneyna.choogoomoneyna_be.mission.mapper.MissionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MissionServiceInterface implements MissionService {

    private final MissionMapper missionMapper;

    @Override
    public int getMissionScore(Integer missionId) {
        return missionMapper.getMissionScore(missionId);
    }
}
