package com.choogoomoneyna.choogoomoneyna_be.matching.service;

import com.choogoomoneyna.choogoomoneyna_be.matching.mapper.RoundInfoMapper;
import com.choogoomoneyna.choogoomoneyna_be.matching.vo.RoundInfoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class RoundInfoServiceImpl implements RoundInfoService {

    private final RoundInfoMapper roundInfoMapper;

    @Override
    public void createRoundInfo(RoundInfoVO roundInfoVO) {
        roundInfoMapper.insertRoundInfo(roundInfoVO);
    }

    @Override
    public RoundInfoVO getLatestRoundInfo() {
        return roundInfoMapper.findLatestRoundInfo();
    }

    @Override
    public Integer getRoundNumber(Integer roundNumber) {
        return roundInfoMapper.getRoundNumber(roundNumber);
    }

    @Override
    public Date getStartDate(Integer roundNumber) {
        return roundInfoMapper.getStartDate(roundNumber);
    }

    @Override
    public Date getEndDate(Integer roundNumber) {
        return roundInfoMapper.getEndDate(roundNumber);
    }
}
