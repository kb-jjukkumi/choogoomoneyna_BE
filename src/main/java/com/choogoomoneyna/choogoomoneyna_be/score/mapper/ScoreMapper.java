package com.choogoomoneyna.choogoomoneyna_be.score.mapper;

import com.choogoomoneyna.choogoomoneyna_be.score.vo.UserScoreVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ScoreMapper {
    int insertScore(UserScoreVO userScoreVO);

    int getScore(Long userId);

    void updateScore(UserScoreVO userScoreVO);

    List<UserScoreVO> findAllScores();

    List<UserScoreVO> findTopNScores(int limit);

    int deleteByUserId(Long userId);
}
