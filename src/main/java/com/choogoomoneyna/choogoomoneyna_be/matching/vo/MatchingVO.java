package com.choogoomoneyna.choogoomoneyna_be.matching.vo;

import com.choogoomoneyna.choogoomoneyna_be.matching.dto.MatchingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class MatchingVO {
    private Long id;
    private Long user1Id;
    private Long user2Id;
    private String matchingStatus;
    private Date matchingStart;
    private Date matchingFinish;
}
