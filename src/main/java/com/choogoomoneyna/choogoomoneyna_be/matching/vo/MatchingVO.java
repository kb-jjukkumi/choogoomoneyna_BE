package com.choogoomoneyna.choogoomoneyna_be.matching.vo;

import com.choogoomoneyna.choogoomoneyna_be.matching.dto.MatchingStatus;

import java.util.Date;

public class MatchingVO {
    private int id;
    private long user1Id;
    private long user2Id;
    private String matchingStatus;
    private Date matchingStart;
    private Date matchingFinish;
}
