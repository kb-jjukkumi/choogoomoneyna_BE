package com.choogoomoneyna.choogoomoneyna_be.user.controller;

import com.choogoomoneyna.choogoomoneyna_be.auth.jwt.CustomUserDetails;
import com.choogoomoneyna.choogoomoneyna_be.matching.service.RoundInfoService;
import com.choogoomoneyna.choogoomoneyna_be.score.service.ScoreService;
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
    private final ScoreService scoreService;

    @GetMapping("/main-profile")
    ResponseEntity<UserMainResponseDTO> getMainProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getId();
        Integer roundNumber = roundInfoService.getRoundNumber();

        UserMainResponseDTO dto = UserConverter.toUserMainResponseDTO(
                    userMainInfoService.findUserMainResponseByUserIdAndRoundNumber(userId, roundNumber)
            );

        // 보여줬으면 다시 false 로 변경
        scoreService.updateIsLevelUpByUserId(userId, false);

        return ResponseEntity.ok(dto);
    }
}
