package com.choogoomoneyna.choogoomoneyna_be.matching.mapper;

import com.choogoomoneyna.choogoomoneyna_be.matching.vo.RoundInfoVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;

@Mapper
public interface RoundInfoMapper {

    void updateRoundInfo(RoundInfoVO roundInfoVO);

    RoundInfoVO findRoundInfo();

    Integer getRoundNumber();

    Date getStartDate();

    Date getEndDate();
}
