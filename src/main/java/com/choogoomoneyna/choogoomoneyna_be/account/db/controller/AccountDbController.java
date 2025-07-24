package com.choogoomoneyna.choogoomoneyna_be.account.db.controller;

import com.choogoomoneyna.choogoomoneyna_be.account.codef.dto.AccountResponseDto;
import com.choogoomoneyna.choogoomoneyna_be.account.db.service.AccountDbService;
import com.choogoomoneyna.choogoomoneyna_be.auth.jwt.JwtTokenProvider;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class AccountDbController {

    private final JwtTokenProvider jwtTokenProvider;
    private final AccountDbService accountDbService;


    @GetMapping("/account/db")
    public ResponseEntity<?> getAccountDb(@RequestHeader("Authorization")String token) {
        try {
            String[] parts = token.split(" ");
            String accessToken = parts[1];
            Long userId = jwtTokenProvider.getUserIdFromToken(accessToken);

            List<AccountResponseDto> accountList = accountDbService.getAllAccounts(userId);

            return ResponseEntity.ok(accountList);

        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/transaction/db")
    public ResponseEntity<?> getTransactionDb() {
        return ResponseEntity.ok(200);
    }
}
