package com.choogoomoneyna.choogoomoneyna_be.user.dto.response;

import com.choogoomoneyna.choogoomoneyna_be.matching.enums.MatchingResult;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class UserMatchingResultHistoryDTO {
    private Integer roundNumber;
    private Date startDate;
    private Date endDate;
    private MatchingResult matchingResult;
}
