package com.choogoomoneyna.choogoomoneyna_be.user.service;

import com.choogoomoneyna.choogoomoneyna_be.user.dto.ChoogooMi;
import com.choogoomoneyna.choogoomoneyna_be.user.dto.LoginType;
import com.choogoomoneyna.choogoomoneyna_be.user.dto.request.UserJoinRequestDTO;

public interface UserService {

    /**
     * 시스템에 새로운 사용자를 등록합니다.
     *
     * @param dto 사용자의 이메일, 비밀번호, 닉네임, 프로필 이미지,
     *            선호 타입(choogooMi)을 포함한 사용자 등록 요청
     */
    void registerUser(UserJoinRequestDTO dto);

    /**
     * 주어진 사용자 닉네임이 시스템에 이미 중복되어 있는지 확인합니다.
     *
     * @param nickname 중복 확인할 사용자의 닉네임
     * @return nickname이 중복되면 true, 그렇지 않으면 false
     */
    public boolean isUserLoginIdDuplicated(String nickname);

    /**
     * 주어진 이메일과 로그인 타입으로 사용자가 존재하는지 확인합니다.
     *
     * @param email 검색할 사용자의 이메일 주소
     * @param loginType 사용자의 로그인 타입 (예: LOCAL, OAUTH2)
     * @return 해당 이메일과 로그인 타입을 가진 사용자가 존재하면 true, 그렇지 않으면 false
     */
    public boolean findByEmailAndLoginType(String email, LoginType loginType);

    /**
     * 이메일 주소로 식별된 사용자의 ChoogooMi(선호 타입)를 업데이트합니다.
     *
     * @param email ChoogooMi를 업데이트할 사용자의 이메일 주소
     * @param choogooMi 사용자에게 설정할 새로운 ChoogooMi 값
     */
    public void updateChoogooMiByEmail(String email, ChoogooMi choogooMi);

}
