package com.choogoomoneyna.choogoomoneyna_be.auth.jwt.service;

import com.choogoomoneyna.choogoomoneyna_be.user.dto.request.UserJoinRequestDTO;

public interface AuthService {

    /**
     * 시스템에 새로운 사용자를 등록합니다.
     *
     * @param dto 사용자의 이메일, 비밀번호, 닉네임, 프로필 이미지,
     *            선호 타입(choogooMi)을 포함한 사용자 등록 요청
     */
    void registerUser(UserJoinRequestDTO dto);


}
