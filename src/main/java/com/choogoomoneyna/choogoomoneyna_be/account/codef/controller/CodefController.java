package com.choogoomoneyna.choogoomoneyna_be.account.codef.controller;

import com.choogoomoneyna.choogoomoneyna_be.account.codef.dto.AccountRequestDto;
import com.choogoomoneyna.choogoomoneyna_be.account.codef.dto.AccountResponseDto;
import com.choogoomoneyna.choogoomoneyna_be.account.codef.dto.AccountUpdateRequestDto;
import com.choogoomoneyna.choogoomoneyna_be.account.codef.service.CodefService;
import com.choogoomoneyna.choogoomoneyna_be.auth.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/codef")
@Slf4j
@RequiredArgsConstructor
public class CodefController {

    private final JwtTokenProvider jwtTokenProvider;
    private final CodefService codefService;

    @PostMapping("/account/add")
    public ResponseEntity<?> addAccount(@RequestHeader("Authorization")String token,
                                        @RequestBody AccountRequestDto accountRequestDto) {
        try {
            //token에서 userId 추출
            String[] parts = token.split(" ");                 // ["Bearer", "eyJhbGciOi..."]
            String accessToken = parts[1];
            Long userId = jwtTokenProvider.getUserIdFromToken(accessToken);

            //codef api 호출 후, db에 저장
            List<AccountResponseDto> accountList = codefService.addAccount(userId, accountRequestDto);

            // 3. 계좌 정보가 없을 때 처리
            if (accountList == null || accountList.isEmpty()) {
                log.info("None account added.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("check id/password.");
            }
            return ResponseEntity.ok(accountList);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @PostMapping("/account/update")
    public ResponseEntity<?> updateAccount(@RequestHeader("Authorization")String token,
                                           @RequestBody AccountUpdateRequestDto accountUpdateRequestDto) {
        try {
            //token에서 userId 추출
            String[] parts = token.split(" ");                 // ["Bearer", "eyJhbGciOi..."]
            String accessToken = parts[1];
            Long userId = jwtTokenProvider.getUserIdFromToken(accessToken);

            //codef api 호출 후, db에 업데이트
            AccountResponseDto accountUpdated = codefService.updateAccountOne(userId, accountUpdateRequestDto);

            return ResponseEntity.ok(accountUpdated);

        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/transaction/get")
    public ResponseEntity<?> getTransaction() {
        return ResponseEntity.ok(200);
    }







}
