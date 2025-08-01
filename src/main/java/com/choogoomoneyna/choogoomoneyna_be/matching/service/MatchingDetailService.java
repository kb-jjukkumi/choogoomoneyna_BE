package com.choogoomoneyna.choogoomoneyna_be.matching.service;

import com.choogoomoneyna.choogoomoneyna_be.matching.dto.Response.MatchingMainResponseDTO;

public interface MatchingDetailService {

    public MatchingMainResponseDTO getMatchingDetail(Long userId, Integer roundNumber);

}
