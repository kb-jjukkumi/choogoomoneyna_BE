package com.choogoomoneyna.choogoomoneyna_be.matching.vo;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMatchingHistoryVO {
    private Long id;
    private Long userId;
    private Long matchingId;
    private Integer roundNumber;
    private String matchingResult;
}
