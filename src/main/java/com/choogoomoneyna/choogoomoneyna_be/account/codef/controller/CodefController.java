package com.choogoomoneyna.choogoomoneyna_be.account.codef.controller;

import com.choogoomoneyna.choogoomoneyna_be.account.codef.dto.*;
import com.choogoomoneyna.choogoomoneyna_be.account.codef.service.CodefService;
import com.choogoomoneyna.choogoomoneyna_be.auth.jwt.util.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
        Long userId = userDetails.getId();

        //codef api 호출 후, db에 저장
        List<AccountResponseDto> accountList = codefService.addAccount(userId, accountRequestDto);

        // 3. 계좌 정보가 없을 때 처리
        if (accountList == null || accountList.isEmpty()) {
            log.info("None account added.");
            return ResponseEntity.status(HttpStatus.OK).body("Already added Accounts");
        }
        return ResponseEntity.ok(accountList);
    }

    @PostMapping("/account/update")
    public ResponseEntity<?> updateAccount(@AuthenticationPrincipal CustomUserDetails userDetails,
                                           @RequestBody AccountUpdateRequestDto accountUpdateRequestDto) {
        Long userId = userDetails.getId();

        //codef api 호출 후, db에 업데이트
        AccountResponseDto accountUpdated = codefService.updateAccountOne(userId, accountUpdateRequestDto);

        return ResponseEntity.ok(accountUpdated);
    }

    @PostMapping("/transaction/add")
    public ResponseEntity<?> addTransaction(@AuthenticationPrincipal CustomUserDetails userDetails,
                                            @RequestBody TransactionRequestDto transactionRequestDto) {
        Long userId = userDetails.getId();
        codefService.addTransaction(userId, transactionRequestDto);
        return ResponseEntity.ok(200);
    }

}
