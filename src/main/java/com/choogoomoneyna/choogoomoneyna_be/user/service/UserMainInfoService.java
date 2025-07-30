package com.choogoomoneyna.choogoomoneyna_be.user.service;

import com.choogoomoneyna.choogoomoneyna_be.user.vo.UserMainInfoVO;

public interface UserMainInfoService {

    UserMainInfoVO findUserMainResponseByUserIdAndRoundNumber(Long userId, Integer roundNumber);
}
