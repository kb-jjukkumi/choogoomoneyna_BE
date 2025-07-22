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
    public void updateRoundInfo(RoundInfoVO roundInfoVO) {
        roundInfoMapper.updateRoundInfo(roundInfoVO);
    }

    @Override
    public RoundInfoVO getRoundInfo() {
        return roundInfoMapper.findRoundInfo();
    }

    @Override
    public Integer getRoundNumber() {
        return roundInfoMapper.getRoundNumber();
    }

    @Override
    public Date getStartDate() {
        return roundInfoMapper.getStartDate();
    }

    @Override
    public Date getEndDate() {
        return roundInfoMapper.getEndDate();
    }
}
