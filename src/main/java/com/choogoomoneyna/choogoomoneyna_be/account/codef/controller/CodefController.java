package com.choogoomoneyna.choogoomoneyna_be.account.codef.controller;

import com.choogoomoneyna.choogoomoneyna_be.account.codef.dto.*;
import com.choogoomoneyna.choogoomoneyna_be.account.codef.service.CodefService;
import com.choogoomoneyna.choogoomoneyna_be.account.codef.vo.TransactionVO;
import com.choogoomoneyna.choogoomoneyna_be.auth.jwt.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/codef")
@Slf4j
@RequiredArgsConstructor
public class CodefController {

    private final CodefService codefService;

    @PostMapping("/account/add")
    public ResponseEntity<?> addAccount(@AuthenticationPrincipal CustomUserDetails userDetails,
                                        @RequestBody AccountRequestDto accountRequestDto) {
        try {
            Long userId = userDetails.getId();

            //codef api 호출 후, db에 저장
            List<AccountResponseDto> accountList = codefService.addAccount(userId, accountRequestDto);

            // 3. 계좌 정보가 없을 때 처리
            if (accountList == null || accountList.isEmpty()) {
                log.info("None account added.");
                return ResponseEntity.status(HttpStatus.OK).body("Already added Accounts");
            }
            return ResponseEntity.ok(accountList);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @PostMapping("/account/update")
    public ResponseEntity<?> updateAccount(@AuthenticationPrincipal CustomUserDetails userDetails,
                                           @RequestBody AccountUpdateRequestDto accountUpdateRequestDto) {
        try {
            Long userId = userDetails.getId();

            //codef api 호출 후, db에 업데이트
            AccountResponseDto accountUpdated = codefService.updateAccountOne(userId, accountUpdateRequestDto);

            return ResponseEntity.ok(accountUpdated);

        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/transaction/add")
    public ResponseEntity<?> addTransaction(@AuthenticationPrincipal CustomUserDetails userDetails,
                                            @RequestBody TransactionRequestDto transactionRequestDto) {
        try {
            Long userId = userDetails.getId();
            codefService.addTransaction(userId, transactionRequestDto);
            return ResponseEntity.ok(200);
        } catch (Exception e) {
            log.error("transaction add failed {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("거래 내역 저장 중 오류가 발생했습니다.");
        }
    }

}
