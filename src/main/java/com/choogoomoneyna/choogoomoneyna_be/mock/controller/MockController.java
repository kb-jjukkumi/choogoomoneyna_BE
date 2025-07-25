package com.choogoomoneyna.choogoomoneyna_be.mock.controller;

import com.choogoomoneyna.choogoomoneyna_be.mock.service.MockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/mock")
public class MockController {

    private final MockService mockService;

    @GetMapping("/create/user")
    public ResponseEntity<?> createMockUser(@RequestParam("count") int count) {
        log.info("Create mock user controller start");
        mockService.createMockUser(count);
        return ResponseEntity.ok().build();
    }

}
