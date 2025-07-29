package com.choogoomoneyna.choogoomoneyna_be.ranking.service;

import com.choogoomoneyna.choogoomoneyna_be.ranking.dto.response.RankingResponseDTO;
import com.choogoomoneyna.choogoomoneyna_be.ranking.vo.RankingUserVO;

public class RankingConverter {

    public static RankingResponseDTO toRankingResponseDTO(RankingUserVO rankingUserVO) {
        return RankingResponseDTO.builder()
                .userNickname(rankingUserVO.getNickname())
                .score(rankingUserVO.getScore())
                .ranking(rankingUserVO.getRanking())
                .build();
    }
}
