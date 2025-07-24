package com.choogoomoneyna.choogoomoneyna_be.matching.vo;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchingVO {
    private Long id;
    private Long user1Id;
    private Integer roundNumber;
    private Long user2Id;
    private String matchingStatus;
}
