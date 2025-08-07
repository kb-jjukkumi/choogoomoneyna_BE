package com.choogoomoneyna.choogoomoneyna_be.auth.oauth.service;

import com.choogoomoneyna.choogoomoneyna_be.auth.oauth.dto.OAuthUserInfoDTO;
import com.choogoomoneyna.choogoomoneyna_be.config.KakaoOAuthConfig;
import com.choogoomoneyna.choogoomoneyna_be.user.enums.LoginType;
import com.choogoomoneyna.choogoomoneyna_be.user.service.UserService;
import com.choogoomoneyna.choogoomoneyna_be.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoLoginService implements OAuthLoginService {

    private final RestTemplate restTemplate;
    private final KakaoOAuthConfig kakaoOAuthConfig;
    private final UserService userService;

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
    public OAuthUserInfoDTO getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(kakaoOAuthConfig.getUserInfoUri(), HttpMethod.GET, request, Map.class);

        Map<String, Object> kakaoAccount = (Map<String, Object>) response.getBody().get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        return OAuthUserInfoDTO.builder()
                .oAuthEmail("kakao" + response.getBody().get("id") + "@social.kakao")
                .nickname((String) profile.get("nickname"))
                .build();
    }

    @Override
    public UserVO findOrCreateUserByOAuth(OAuthUserInfoDTO dto) {
        UserVO user = userService.findByEmailAndLoginType(dto.getOAuthEmail(), LoginType.KAKAO);

        if (user == null) {
            Date now = new Date();
            user = UserVO.builder()
                    .email(dto.getOAuthEmail())
                    .password("<PASSWORD>")
                    .nickname(dto.getNickname())
                    .loginType(LoginType.KAKAO.name())
                    .regDate(now)
                    .updateDate(now)
                    .choogooMi(LoginType.KAKAO.name())
                    .build();
            userService.insertUser(user);
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
