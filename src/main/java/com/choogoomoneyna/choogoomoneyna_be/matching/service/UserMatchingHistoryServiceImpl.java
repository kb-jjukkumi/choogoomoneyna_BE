package com.choogoomoneyna.choogoomoneyna_be.matching.service;

import com.choogoomoneyna.choogoomoneyna_be.matching.mapper.UserMatchingHistoryMapper;
import com.choogoomoneyna.choogoomoneyna_be.matching.vo.UserMatchingHistoryVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserMatchingHistoryServiceImpl implements UserMatchingHistoryService {

    private final UserMatchingHistoryMapper userMatchingHistoryMapper;

    @Override
    public void insertUserMatchingHistory(UserMatchingHistoryVO userMatchingHistoryVO) {
        userMatchingHistoryMapper.insertUserMatchingHistory(userMatchingHistoryVO);
    }

    @Override
    public List<UserMatchingHistoryVO> findAllUserMatchingHistoryByUserId(Long userId) {
        return userMatchingHistoryMapper.findAllUserMatchingHistoryByUserId(userId);
    }
}
