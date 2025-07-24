package com.choogoomoneyna.choogoomoneyna_be.account.codef.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountVO {
    private String accountNum;
    private Long userId;
    private String bankId;
    private String accountName;
    private String accountBalance;
    private LocalDateTime updateDate;
}
