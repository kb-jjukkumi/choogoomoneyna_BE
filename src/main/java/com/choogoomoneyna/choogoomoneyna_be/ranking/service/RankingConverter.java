package com.choogoomoneyna.choogoomoneyna_be.ranking.service;

import com.choogoomoneyna.choogoomoneyna_be.ranking.dto.response.RankingHistoryDTO;
import com.choogoomoneyna.choogoomoneyna_be.ranking.dto.response.RankingResponseDTO;
import com.choogoomoneyna.choogoomoneyna_be.ranking.vo.RankingUserVO;
import com.choogoomoneyna.choogoomoneyna_be.ranking.vo.RankingVO;

public class RankingConverter {

    public static RankingResponseDTO toRankingResponseDTO(RankingUserVO rankingUserVO) {
        return RankingResponseDTO.builder()
                .userNickname(rankingUserVO.getNickname())
                .score(rankingUserVO.getScore())
                .ranking(rankingUserVO.getRanking())
                .build();
    }

    public static RankingHistoryDTO toRankingHistoryDTO(RankingVO rankingVO) {
        return RankingHistoryDTO.builder()
                .regDate(rankingVO.getRegDate())
                .ranking(rankingVO.getCurrentRanking())
                .build();
    }
}
