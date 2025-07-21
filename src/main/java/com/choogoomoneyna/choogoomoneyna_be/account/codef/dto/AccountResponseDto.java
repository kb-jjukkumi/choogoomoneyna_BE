package com.choogoomoneyna.choogoomoneyna_be.account.codef.dto;

import lombok.Data;

@Data
public class AccountResponseDto {
    private String accountNum;
    private String accountName;
    private String bankId;
    private String accountBalance;
}
