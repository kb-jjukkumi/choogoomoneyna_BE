package com.choogoomoneyna.choogoomoneyna_be.user.controller;

import com.choogoomoneyna.choogoomoneyna_be.auth.jwt.util.CustomUserDetails;
import com.choogoomoneyna.choogoomoneyna_be.matching.service.RoundInfoService;
import com.choogoomoneyna.choogoomoneyna_be.score.service.ScoreService;
import com.choogoomoneyna.choogoomoneyna_be.user.dto.request.UserUpdateRequestDTO;
import com.choogoomoneyna.choogoomoneyna_be.user.dto.response.UserMainResponseDTO;
import com.choogoomoneyna.choogoomoneyna_be.user.dto.response.UserMatchingResultHistoryDTO;
import com.choogoomoneyna.choogoomoneyna_be.user.service.UserConverter;
import com.choogoomoneyna.choogoomoneyna_be.user.service.UserMainInfoService;
import com.choogoomoneyna.choogoomoneyna_be.user.service.UserMatchingResultHistoryService;
import com.choogoomoneyna.choogoomoneyna_be.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final RoundInfoService roundInfoService;
    private final UserService userService;
    private final UserMainInfoService userMainInfoService;
    private final ScoreService scoreService;
    private final UserMatchingResultHistoryService userMatchingResultHistoryService;
    private final PasswordEncoder passwordEncoder;

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

    @GetMapping("/matching/result/history")
    ResponseEntity<List<UserMatchingResultHistoryDTO>> getMatchingResultHistory(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getId();

        List<UserMatchingResultHistoryDTO> userMatchingResultHistoryDTOList
                = userMatchingResultHistoryService.findUserMatchingResultHistoriesByUserId(userId)
                .stream()
                .map(UserConverter::toUserMatchingResultHistoryDTO)
                .toList();

        return ResponseEntity.ok(userMatchingResultHistoryDTOList);
    }

    @PutMapping("/update")
    ResponseEntity<String> updateUserNicknameAndPassword(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody UserUpdateRequestDTO request
            ) {

        Long userId = userDetails.getId();
        String nickname = request.getNickname();
        String password = request.getPassword();
        String newPassword = request.getNewPassword();
        String newPasswordConfirm = request.getNewPasswordConfirm();

        String encodedPassword = userService.getPasswordByUserId(userId);
        if (password == null || !passwordEncoder.matches(password, encodedPassword)) {
            return ResponseEntity.badRequest().body("password not match");
        }

        if (newPassword == null || !newPassword.equals(newPasswordConfirm)) {
            return ResponseEntity.badRequest().body("new password not match");
        }

        String encodedNewPassword = passwordEncoder.encode(newPassword);
        userService.updateNicknameAndPasswordByUserId(userId, nickname, encodedNewPassword);

        return ResponseEntity.ok("user updated");
    }
}
