package com.choogoomoneyna.choogoomoneyna_be.user.service;

import com.choogoomoneyna.choogoomoneyna_be.exception.CustomException;
import com.choogoomoneyna.choogoomoneyna_be.exception.ErrorCode;
import com.choogoomoneyna.choogoomoneyna_be.user.mapper.UserMainInfoMapper;
import com.choogoomoneyna.choogoomoneyna_be.user.vo.UserMainInfoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserMainInfoServiceImpl implements UserMainInfoService {

    private final UserMainInfoMapper userMainInfoMapper;

    @Override
    public UserMainInfoVO findUserMainResponseByUserIdAndRoundNumber(Long userId, Integer roundNumber) {
        UserMainInfoVO vo = userMainInfoMapper.findUserMainResponseByUserIdAndRoundNumber(userId, roundNumber);
        if (vo == null) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        System.out.println(vo.getUserId() + " " + vo.getUserScore() + " " + vo.getNickname());
        return vo;
    }
}
