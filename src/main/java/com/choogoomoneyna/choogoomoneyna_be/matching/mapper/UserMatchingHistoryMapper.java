package com.choogoomoneyna.choogoomoneyna_be.matching.mapper;

import com.choogoomoneyna.choogoomoneyna_be.matching.vo.UserMatchingHistoryVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMatchingHistoryMapper {

    void insertUserMatchingHistory(UserMatchingHistoryVO userMatchingHistoryVO);

    List<UserMatchingHistoryVO> findAllUserMatchingHistoryByUserId(Long userId);
}
