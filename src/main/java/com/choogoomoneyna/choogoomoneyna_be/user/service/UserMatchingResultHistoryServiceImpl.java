package com.choogoomoneyna.choogoomoneyna_be.user.service;

import com.choogoomoneyna.choogoomoneyna_be.user.mapper.UserMatchingResultHistoryMapper;
import com.choogoomoneyna.choogoomoneyna_be.user.vo.UserMatchingResultHistoryVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserMatchingResultHistoryServiceImpl implements UserMatchingResultHistoryService {

    private final UserMatchingResultHistoryMapper userMatchingResultHistoryMapper;

    @Override
    public List<UserMatchingResultHistoryVO> findUserMatchingResultHistoriesByUserId(Long userId) {
        return userMatchingResultHistoryMapper.findUserMatchingResultHistoriesByUserId(userId);
    }
}
