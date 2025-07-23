package com.choogoomoneyna.choogoomoneyna_be.account.codef.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionVO {
    private Long transactionId;       // 거래내역 아이디
    private String accountNum;        // 계좌번호

    private Integer trTime;           // 거래시각
    private Integer trAccountOut;     // 출금금액
    private Integer trAccountIn;      // 입금금액
    private Integer trAfterBalance;   // 거래 후 잔액

    private String trDesc1;           // 비고1 (보낸분, 받은분)
    private String trDesc2;           // 비고2 (메모)
    private String trDesc3;           // 비고3 (적요)
    private String trDesc4;           // 비고4 (거래점)
}
