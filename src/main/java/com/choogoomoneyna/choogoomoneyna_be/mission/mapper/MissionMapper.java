package com.choogoomoneyna.choogoomoneyna_be.mission.mapper;

import com.choogoomoneyna.choogoomoneyna_be.mission.vo.MissionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MissionMapper {
    void insertMission(MissionVO missionVO);

    MissionVO findMissionByMissionId(Integer missionId);

    Integer getMissionScore(Integer missionId);

    Integer getMissionLimitAmount(Integer missionId);

    String getMissionType(Integer missionId);

}
