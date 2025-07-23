package com.choogoomoneyna.choogoomoneyna_be.account.codef.dto;

import lombok.Data;

import java.util.List;

@Data
public class TransactionResponseDto {
    private String accountNum;        // 계좌번호
    private List<trItem> transactionList;

    @Data
    public static class trItem {
        private Long transactionId;       // 거래 고유 ID
        private String trDate;            // 거래일 (yyyy-MM-dd 형식)
        private int trTime;               // 거래시간 (HHmmss 정수)
        private String trAccountOut;        // 출금액
        private String trAccountIn;         // 입금액
        private String trAfterBalance;      // 거래 후 잔액
        private String transactionType;     //입금 or 출금
        private String trDesc1;           // 보내는 분,받는 분
        private String trDesc2;           // 메모
        private String trDesc3;           // 적요
        private String trDesc4;           // 거래점
    }
}
