package com.choogoomoneyna.choogoomoneyna_be.auth.oauth.service;

import com.choogoomoneyna.choogoomoneyna_be.auth.oauth.dto.OAuthUserInfoDTO;
import com.choogoomoneyna.choogoomoneyna_be.user.vo.UserVO;

public interface OAuthLoginService {

    /**
     * OAuth 공급자로부터 인증 코드를 사용하여 액세스 토큰을 조회합니다.
     * 액세스 토큰은 API 호출 인증 및 사용자 정보 접근에 사용됩니다.
     *
     * @param code OAuth 공급자로부터 성공적인 인증 프로세스 후 받은 인증 코드
     * @return 정상적으로 조회된 경우 액세스 토큰 문자열 반환
     */
    String getAccessToken(String code);

    /**
     * 주어진 액세스 토큰을 사용하여 OAuth 공급자로부터 사용자 정보를 조회합니다.
     *
     * @param accessToken OAuth 공급자로부터 받은 액세스 토큰으로 사용자 정보 조회 및
     *                    인증에 사용됨
     * @return 사용자의 OAuth 이메일과 닉네임이 포함된 {@code OAuthUserInfoDTO} 인스턴스
     */
    OAuthUserInfoDTO getUserInfo(String accessToken);

    /**
     * 제공된 OAuth 사용자 정보를 기반으로 기존 사용자를 찾거나 새로운 사용자를 생성합니다.
     *
     * @param dto 사용자 이메일과 닉네임이 포함된 OAuth 사용자 정보 DTO
     * @return 기존 또는 새로 생성된 {@code UserVO} 인스턴스
     */
    UserVO findOrCreateUserByOAuth(OAuthUserInfoDTO dto);

}