package com.choogoomoneyna.choogoomoneyna_be.user.controller;

import com.choogoomoneyna.choogoomoneyna_be.auth.jwt.CustomUserDetails;
import com.choogoomoneyna.choogoomoneyna_be.matching.service.RoundInfoService;
import com.choogoomoneyna.choogoomoneyna_be.user.dto.response.UserMainResponseDTO;
import com.choogoomoneyna.choogoomoneyna_be.user.service.UserConverter;
import com.choogoomoneyna.choogoomoneyna_be.user.service.UserMainInfoService;
import com.choogoomoneyna.choogoomoneyna_be.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final RoundInfoService roundInfoService;
    private final UserService userService;
    private final UserMainInfoService userMainInfoService;

    @GetMapping("/main-profile")
    ResponseEntity<UserMainResponseDTO> getMainProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getId();
        Integer roundNumber = roundInfoService.getRoundNumber();

        UserMainResponseDTO dto;
        try {
            dto = UserConverter.toUserMainResponseDTO(
                    userMainInfoService.findUserMainResponseByUserIdAndRoundNumber(userId, roundNumber)
            );
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.ok(dto);
    }
}
