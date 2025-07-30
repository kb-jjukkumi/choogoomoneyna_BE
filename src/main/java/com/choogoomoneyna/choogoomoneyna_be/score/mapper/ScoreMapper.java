package com.choogoomoneyna.choogoomoneyna_be.score.mapper;

import com.choogoomoneyna.choogoomoneyna_be.score.vo.UserScoreVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ScoreMapper {
    int insertScore(UserScoreVO userScoreVO);

    int getScoreByUserIdAndRoundNumber(@Param("userId") Long userId, @Param("roundNumber") int roundNumber);

    void updateScore(UserScoreVO userScoreVO);

    List<UserScoreVO> findCurrentAllScores();

    List<UserScoreVO> findTopNCurrentScoresByRoundNumber(
            @Param("roundNumber") int roundNumber,
            @Param("limit") int limit
    );

    void deleteByUserId(Long userId);
}
