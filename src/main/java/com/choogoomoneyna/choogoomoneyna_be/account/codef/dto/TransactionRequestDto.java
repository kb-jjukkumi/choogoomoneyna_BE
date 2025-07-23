package com.choogoomoneyna.choogoomoneyna_be.account.codef.dto;

import lombok.Data;

@Data
public class TransactionRequestDto {
        private String connectedId;  // 커넥티드아이디
        private String account;   // 계좌번호
        private String organization;       // 은행 기관코드
        private String startDate;    // 조회 시작일자 (yyyyMMdd)
        private String endDate;      // 조회 종료일자 (yyyyMMdd)
        private String orderBy;
        private Integer offset;      // 추후 페이징
        private Integer limit;       // 추후 페이징
}
