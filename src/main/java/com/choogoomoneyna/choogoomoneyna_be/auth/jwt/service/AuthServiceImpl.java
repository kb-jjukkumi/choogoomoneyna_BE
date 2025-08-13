package com.choogoomoneyna.choogoomoneyna_be.auth.jwt.service;

import com.choogoomoneyna.choogoomoneyna_be.exception.CustomException;
import com.choogoomoneyna.choogoomoneyna_be.exception.ResponseCode;
import com.choogoomoneyna.choogoomoneyna_be.matching.service.RoundInfoService;
import com.choogoomoneyna.choogoomoneyna_be.ranking.service.RankingService;
import com.choogoomoneyna.choogoomoneyna_be.ranking.vo.RankingVO;
import com.choogoomoneyna.choogoomoneyna_be.score.service.ScoreService;
import com.choogoomoneyna.choogoomoneyna_be.score.vo.UserScoreVO;
import com.choogoomoneyna.choogoomoneyna_be.user.enums.ChoogooMi;
import com.choogoomoneyna.choogoomoneyna_be.user.enums.LoginType;
import com.choogoomoneyna.choogoomoneyna_be.user.dto.request.UserJoinRequestDTO;
import com.choogoomoneyna.choogoomoneyna_be.user.service.UserConverter;
import com.choogoomoneyna.choogoomoneyna_be.user.service.UserService;
import com.choogoomoneyna.choogoomoneyna_be.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RankingService rankingService;
    private final ScoreService scoreService;
    private final RoundInfoService roundInfoService;

    @Override
    @Transactional
    public void registerUser(UserJoinRequestDTO dto) {
        // email 중복 체크
        if (userService.existsByEmailAndLoginType(dto.getEmail(), LoginType.LOCAL)) {
            throw new CustomException(ResponseCode.AUTH_EMAIL_EXISTS, "이미 존재하는 이메일입니다.");
        }
        String encryptedPassword = passwordEncoder.encode(dto.getPassword());

        int roundNumber = roundInfoService.getRoundNumber();

        UserVO userVO = UserConverter.joinRequestDtoToVo(dto, LoginType.LOCAL, encryptedPassword, ChoogooMi.O);
        userService.insertUser(userVO);
        Long userId = userService.findByEmailAndLoginType(dto.getEmail(), LoginType.LOCAL).getId();

        rankingService.createRanking(RankingVO.builder()
                .roundNumber(roundNumber)
                .userId(userId)
                .build());

        scoreService.createScore(UserScoreVO.builder()
                .roundNumber(roundNumber)
                .userId(userId)
                .scoreValue(0)
                .isLevelUp(false)
                .build());
        log.info("User registered: {}", userVO);
    }
}
