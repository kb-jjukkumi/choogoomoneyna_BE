package com.choogoomoneyna.choogoomoneyna_be.account.codef.vo;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class CodefTokenVO {
    private Long id;
    private String accessToken;
    private Timestamp tokenExpiryTime;
    private Timestamp createdAt;
}
