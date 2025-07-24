package com.choogoomoneyna.choogoomoneyna_be.account.db.service;

import com.choogoomoneyna.choogoomoneyna_be.account.codef.vo.TransactionVO;
import com.choogoomoneyna.choogoomoneyna_be.account.db.dto.TransactionItemDto;

public class TransactionConverter {

    public static TransactionItemDto voToItemDto(TransactionVO vo) {
        if(vo == null)
            return null;

        return TransactionItemDto.builder()
                .transactionId(vo.getTransactionId())
                .trTime(vo.getTrTime().toString())
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
