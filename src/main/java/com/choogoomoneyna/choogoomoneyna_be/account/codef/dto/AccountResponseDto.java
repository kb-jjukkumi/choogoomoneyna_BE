package com.choogoomoneyna.choogoomoneyna_be.account.codef.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponseDto {
    private String accountNum;
    private String accountName;
    private String bankId;
    private String accountBalance;
}
