package com.choogoomoneyna.choogoomoneyna_be.account.db.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class AccountDbController {

    @GetMapping("/account/db")
    public ResponseEntity<?> getAccountDb() {
        return ResponseEntity.ok(200);
    }

    @GetMapping("/transaction/db")
    public ResponseEntity<?> getTransactionDb() {
        return ResponseEntity.ok(200);
    }
}
