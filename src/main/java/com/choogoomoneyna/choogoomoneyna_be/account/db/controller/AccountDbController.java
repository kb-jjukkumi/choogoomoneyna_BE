package com.choogoomoneyna.choogoomoneyna_be.account.db.controller;

import com.choogoomoneyna.choogoomoneyna_be.account.codef.dto.AccountResponseDto;
import com.choogoomoneyna.choogoomoneyna_be.account.db.dto.TransactionItemDto;
import com.choogoomoneyna.choogoomoneyna_be.account.db.service.AccountDbService;
import com.choogoomoneyna.choogoomoneyna_be.auth.jwt.util.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class AccountDbController {

    private final AccountDbService accountDbService;


    @GetMapping("/account/db")
    public ResponseEntity<?> getAccountDb(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getId();

        List<AccountResponseDto> accountList = accountDbService.getAllAccounts(userId);

        return ResponseEntity.ok(accountList);
    }

    @GetMapping("/transaction/db")
    public ResponseEntity<?> getTransactionDb(@AuthenticationPrincipal CustomUserDetails userDetails,
                                              @RequestParam String account) {
        Long userId = userDetails.getId();
        List<TransactionItemDto> transactions = accountDbService.getAllTransactions(account);
        return ResponseEntity.ok(transactions);
    }
}
