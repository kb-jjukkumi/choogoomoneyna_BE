package com.choogoomoneyna.choogoomoneyna_be.auth.oauth.service;

import com.choogoomoneyna.choogoomoneyna_be.auth.jwt.service.RefreshTokenService;
import com.choogoomoneyna.choogoomoneyna_be.auth.jwt.util.JwtTokenProvider;
import com.choogoomoneyna.choogoomoneyna_be.auth.oauth.dto.response.OAuthUserInfoResponseDTO;
import com.choogoomoneyna.choogoomoneyna_be.config.KakaoOAuthConfig;
import com.choogoomoneyna.choogoomoneyna_be.exception.CustomException;
import com.choogoomoneyna.choogoomoneyna_be.exception.ErrorCode;
import com.choogoomoneyna.choogoomoneyna_be.matching.service.RoundInfoService;
import com.choogoomoneyna.choogoomoneyna_be.ranking.service.RankingService;
import com.choogoomoneyna.choogoomoneyna_be.ranking.vo.RankingVO;
import com.choogoomoneyna.choogoomoneyna_be.score.service.ScoreService;
import com.choogoomoneyna.choogoomoneyna_be.score.vo.UserScoreVO;
import com.choogoomoneyna.choogoomoneyna_be.user.dto.request.JwtTokenResponseDTO;
import com.choogoomoneyna.choogoomoneyna_be.user.enums.ChoogooMi;
import com.choogoomoneyna.choogoomoneyna_be.user.enums.LoginType;
import com.choogoomoneyna.choogoomoneyna_be.user.service.UserService;
import com.choogoomoneyna.choogoomoneyna_be.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoLoginService implements OAuthLoginService {

    private final RestTemplate restTemplate;
    private final KakaoOAuthConfig kakaoOAuthConfig;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RoundInfoService roundInfoService;
    private final RankingService rankingService;
    private final ScoreService scoreService;

    @Override
    public JwtTokenResponseDTO login(String code) {
        // OAuth 공급자로부터 사용자 정보 조회
        OAuthUserInfoResponseDTO userInfo = getUserInfo(getAccessToken(code));

        // 사용자 정보로 기존 회원 조회 또는 신규 회원 생성
        UserVO user = findOrCreateUserByOAuth(userInfo);

        // 기존 리프레시 토큰 모두 삭제 
        refreshTokenService.deleteAllTokensByUserId(user.getId());

        // 새로운 액세스 토큰과 리프레시 토큰 생성
        String accessToken = jwtTokenProvider.generateAccessToken(user.getId());
        String refreshToken = refreshTokenService.generateRefreshTokenAndSave(user.getId());

        return new JwtTokenResponseDTO(accessToken, refreshToken);
    }

    @Override
    public String getAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoOAuthConfig.getClientId());
        body.add("redirect_uri", kakaoOAuthConfig.getRedirectUri());
        body.add("client_secret", kakaoOAuthConfig.getClientSecret());
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    kakaoOAuthConfig.getTokenUri(),
                    request,
                    Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return (String) response.getBody().get("access_token");
            }

            throw new RuntimeException("카카오 액세스 토큰 획득 실패");

        } catch (HttpClientErrorException e) {
            log.error("카카오 OAuth 에러: {}", e.getStatusCode());
            log.error("요청 정보: client_id={}, redirect_uri={}",
                    kakaoOAuthConfig.getClientId(),
                    kakaoOAuthConfig.getRedirectUri()
            );
            throw new RuntimeException("카카오 인증 처리 중 오류가 발생했습니다", e);
        }
    }

    @Override
    public OAuthUserInfoResponseDTO getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(kakaoOAuthConfig.getUserInfoUri(), HttpMethod.GET, request, Map.class);

        Map<String, Object> kakaoAccount = (Map<String, Object>) response.getBody().get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        return OAuthUserInfoResponseDTO.builder()
                .oAuthEmail("kakao" + response.getBody().get("id") + "@social.kakao")
                .nickname("kakao_" + profile.get("nickname"))
                .build();
    }

    @Override
    public UserVO findOrCreateUserByOAuth(OAuthUserInfoResponseDTO dto) {
        UserVO user = userService.findByEmailAndLoginType(dto.getOAuthEmail(), LoginType.KAKAO);
        System.out.println("UserVO: " + user);

        if (user == null) {
            try {
                String rawPassword = UUID.randomUUID().toString();
                String encryptedPassword = passwordEncoder.encode(rawPassword);

                int roundNumber = roundInfoService.getRoundNumber();

                Date now = new Date();
                user = UserVO.builder()
                        .email(dto.getOAuthEmail())
                        .password(encryptedPassword)
                        .nickname(dto.getNickname())
                        .loginType(LoginType.KAKAO.name())
                        .regDate(now)
                        .updateDate(now)
                        .choogooMi(ChoogooMi.O.name())
                        .build();

                System.out.println("UserVO: " + user);
                userService.insertUser(user);

                Long userId = userService.findByEmailAndLoginType(dto.getOAuthEmail(), LoginType.KAKAO).getId();
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

            } catch (CustomException e) {
                throw e;
            } catch (Exception e) {
                log.error("카카오 로그인 처리 중 오류 발생", e);
                throw new CustomException(
                        ErrorCode.INTERNAL_SERVER_ERROR,
                        "카카오 로그인 처리 중 알 수 없는 오류가 발생했습니다.",
                        e
                );
            }

        }

        return user;
    }

    public void unlinkKakao(String kakaoAccessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(kakaoAccessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        restTemplate.postForEntity(kakaoOAuthConfig.getUnlinkUri(), request, Void.class);
    }
}
