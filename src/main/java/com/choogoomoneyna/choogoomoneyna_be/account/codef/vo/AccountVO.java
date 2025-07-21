package com.choogoomoneyna.choogoomoneyna_be.account.codef.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AccountVO {
    private String accountNum;
    private Long userId;
    private String bankId;
    private String accountName;
    private int accountBalance;
}
