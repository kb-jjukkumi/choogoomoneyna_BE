package com.choogoomoneyna.choogoomoneyna_be.account.db.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TransactionItemDto {

    private Long transactionId;       // 거래 고유 ID
    private String trTime;            // 거래 날짜,시간 "2025-07-23T12:34:25"
    private int trAccountOut;        // 출금액
    private int trAccountIn;         // 입금액
    private int trAfterBalance;      // 거래 후 잔액
    private String transactionType;     //입금 or 출금
    private String trDesc1;           // 보내는 분,받는 분
    private String trDesc2;           // 메모
    private String trDesc3;           // 적요
    private String trDesc4;           // 거래점
}