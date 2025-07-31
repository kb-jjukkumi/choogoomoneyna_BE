package com.choogoomoneyna.choogoomoneyna_be.user.vo;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMatchingResultHistoryVO {
    private Long matchingId;
    private Integer roundNumber;
    private Date startDate;
    private Date endDate;
    private String matchingResult;
}
