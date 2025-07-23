package com.choogoomoneyna.choogoomoneyna_be.mission.mapper;

import com.choogoomoneyna.choogoomoneyna_be.mission.vo.MissionVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MissionMapper {
    void insertMission(MissionVO missionVO);

    MissionVO findMissionByMissionId(Integer missionId);

    Integer getMissionScore(Integer missionId);
}
