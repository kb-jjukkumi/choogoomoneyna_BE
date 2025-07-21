package com.choogoomoneyna.choogoomoneyna_be.account.codef.dto;

import lombok.Data;

@Data
public class AccountRequestDto {
    private String bankId;
    private String userBankId;
    private String userBankPassword;
}
