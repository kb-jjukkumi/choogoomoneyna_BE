package com.choogoomoneyna.choogoomoneyna_be.user.service;

import com.choogoomoneyna.choogoomoneyna_be.user.vo.UserMatchingResultHistoryVO;

import java.util.List;

public interface UserMatchingResultHistoryService {

    List<UserMatchingResultHistoryVO> findUserMatchingResultHistoriesByUserId(Long userId);

}
