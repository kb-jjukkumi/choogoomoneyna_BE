package com.choogoomoneyna.choogoomoneyna_be.account.db.service;

import com.choogoomoneyna.choogoomoneyna_be.account.codef.vo.TransactionVO;
import com.choogoomoneyna.choogoomoneyna_be.account.db.dto.TransactionItemDto;

import java.time.format.DateTimeFormatter;

public class TransactionConverter {

    public static TransactionItemDto voToItemDto(TransactionVO vo) {
        if(vo == null)
            return null;

        // 1. 포맷 정의
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 2. 포맷 적용
        String formattedTime = vo.getTrTime().format(formatter);

        return TransactionItemDto.builder()
                .transactionId(vo.getTransactionId())
                .trTime(formattedTime)
                .trAccountIn(vo.getTrAccountIn())
                .trAccountOut(vo.getTrAccountOut())
                .trAfterBalance(vo.getTrAfterBalance())
                .transactionType(vo.getTransactionType())
                .trDesc1(vo.getTrDesc1())
                .trDesc2(vo.getTrDesc2())
                .trDesc3(vo.getTrDesc3())
                .trDesc4(vo.getTrDesc4())
                .build();
    }
}
