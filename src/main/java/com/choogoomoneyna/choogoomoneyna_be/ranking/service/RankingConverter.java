package com.choogoomoneyna.choogoomoneyna_be.ranking.service;

import com.choogoomoneyna.choogoomoneyna_be.ranking.dto.response.RankingChangeResponseDTO;
import com.choogoomoneyna.choogoomoneyna_be.ranking.dto.response.RankingHistoryDTO;
import com.choogoomoneyna.choogoomoneyna_be.ranking.dto.response.RankingResponseDTO;
import com.choogoomoneyna.choogoomoneyna_be.ranking.vo.RankingUserChangeVO;
import com.choogoomoneyna.choogoomoneyna_be.ranking.vo.RankingUserVO;
import com.choogoomoneyna.choogoomoneyna_be.ranking.vo.RankingVO;
import com.choogoomoneyna.choogoomoneyna_be.user.enums.ChoogooMi;

public class RankingConverter {

    public static RankingResponseDTO toRankingResponseDTO(RankingUserVO rankingUserVO) {
        return RankingResponseDTO.builder()
                .userNickname(rankingUserVO.getNickname())
                .score(rankingUserVO.getScore())
                .ranking(rankingUserVO.getRanking())
                .choogooMi(ChoogooMi.valueOf(rankingUserVO.getChoogooMi()))
                .build();
    }

    public static RankingChangeResponseDTO toRankingChangeResponseDTO(RankingUserChangeVO rankingUserChangeVO) {
        return RankingChangeResponseDTO.builder()
                .userNickname(rankingUserChangeVO.getNickname())
                .score(rankingUserChangeVO.getScore())
                .ranking(rankingUserChangeVO.getRanking())
                .beforeRanking(rankingUserChangeVO.getBeforeRanking())
                .choogooMi(ChoogooMi.valueOf(rankingUserChangeVO.getChoogooMi()))
                .build();
    }

    public static RankingHistoryDTO toRankingHistoryDTO(RankingVO rankingVO) {
        return RankingHistoryDTO.builder()
                .startDate(rankingVO.getRegDate())
                .ranking(rankingVO.getCurrentRanking())
                .build();
    }
}
