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
        try {
            // email 중복 체크
            if (userService.existsByEmailAndLoginType(dto.getEmail(), LoginType.LOCAL)) {
                throw new CustomException(ResponseCode.AUTH_EMAIL_EXISTS, "이미 존재하는 이메일입니다.");
            }

            // 비밀번호 암호화
            String encryptedPassword = passwordEncoder.encode(dto.getPassword());

            // 현재 라운드 번호 조회
            int roundNumber = roundInfoService.getRoundNumber();

            // UserVO 생성 및 DB 저장
            UserVO userVO = UserConverter.joinRequestDtoToVo(dto, LoginType.LOCAL, encryptedPassword, ChoogooMi.O);
            userService.insertUser(userVO);

            // 저장된 사용자 ID 조회
            Long userId = userService.findByEmailAndLoginType(dto.getEmail(), LoginType.LOCAL).getId();

            // 랭킹 초기화
            rankingService.createRanking(RankingVO.builder()
                    .roundNumber(roundNumber)
                    .userId(userId)
                    .build());

            // 점수 초기화
            scoreService.createScore(UserScoreVO.builder()
                    .roundNumber(roundNumber)
                    .userId(userId)
                    .scoreValue(0)
                    .isLevelUp(false)
                    .build());
            log.info("User registered: {}", userVO);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("회원가입 처리 중 오류 발생", e);
            throw new CustomException(
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "회원가입 처리 중 알 수 없는 오류가 발생했습니다.",
                    e
            );
        }
    }
}
