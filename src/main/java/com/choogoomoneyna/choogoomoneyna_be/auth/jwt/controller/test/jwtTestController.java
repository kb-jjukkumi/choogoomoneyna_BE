package com.choogoomoneyna.choogoomoneyna_be.auth.jwt.controller.test;

import com.choogoomoneyna.choogoomoneyna_be.auth.jwt.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class jwtTestController {

    @GetMapping("/test-auth")
    public ResponseEntity<?> testAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No authentication found");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) principal;
            return ResponseEntity.ok("User ID: " + userDetails.getId());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Principal is not CustomUserDetails");
        }
    }

}
