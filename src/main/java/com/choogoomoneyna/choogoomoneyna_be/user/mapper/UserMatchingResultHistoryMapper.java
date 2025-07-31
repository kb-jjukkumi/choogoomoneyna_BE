package com.choogoomoneyna.choogoomoneyna_be.user.mapper;

import com.choogoomoneyna.choogoomoneyna_be.user.vo.UserMatchingResultHistoryVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMatchingResultHistoryMapper {
    
    List<UserMatchingResultHistoryVO> findUserMatchingResultHistoriesByUserId(Long userId);
}
