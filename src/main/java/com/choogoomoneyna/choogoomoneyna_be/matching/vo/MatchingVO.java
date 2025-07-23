package com.choogoomoneyna.choogoomoneyna_be.matching.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class MatchingVO {
    private Long id;
    private Long user1Id;
    private Integer roundNumber;
    private Long user2Id;
    private String matchingStatus;
}
