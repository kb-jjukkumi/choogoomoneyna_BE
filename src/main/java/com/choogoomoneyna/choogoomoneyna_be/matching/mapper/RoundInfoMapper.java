package com.choogoomoneyna.choogoomoneyna_be.matching.mapper;

import com.choogoomoneyna.choogoomoneyna_be.matching.vo.RoundInfoVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;

@Mapper
public interface RoundInfoMapper {

    void insertRoundInfo(RoundInfoVO roundInfoVO);

    RoundInfoVO findLatestRoundInfo();

    RoundInfoVO findRoundInfoByRoundNumber(Integer roundNumber);

    Integer getRoundNumber(Integer roundNumber);

    Date getStartDate(Integer roundNumber);

    Date getEndDate(Integer roundNumber);
}
