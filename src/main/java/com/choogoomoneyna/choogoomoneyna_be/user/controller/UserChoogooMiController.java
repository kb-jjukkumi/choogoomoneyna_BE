package com.choogoomoneyna.choogoomoneyna_be.user.controller;

import com.choogoomoneyna.choogoomoneyna_be.auth.jwt.util.CustomUserDetails;
import com.choogoomoneyna.choogoomoneyna_be.user.dto.request.UserUpdatedChoogooMiDTO;
import com.choogoomoneyna.choogoomoneyna_be.user.enums.ChoogooMi;
import com.choogoomoneyna.choogoomoneyna_be.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/choogoomi")
public class UserChoogooMiController {

    private final UserService userService;

    @PutMapping("/update")
    ResponseEntity<Map<String, String>> updateChoogooMi(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody UserUpdatedChoogooMiDTO request
    ) {
        Long userId = userDetails.getId();
        ChoogooMi choogooMi = request.getChoogooMi();

        userService.updateChoogooMiByUserId(userId, choogooMi);

        return ResponseEntity.ok(
                Map.of("message", "Choogoo Mi updated")
        );
    }
}
