package com.choogoomoneyna.choogoomoneyna_be.user.service;

import com.choogoomoneyna.choogoomoneyna_be.user.dto.ChoogooMi;
import com.choogoomoneyna.choogoomoneyna_be.user.dto.LoginType;
import com.choogoomoneyna.choogoomoneyna_be.user.dto.request.UserJoinRequestDTO;
import com.choogoomoneyna.choogoomoneyna_be.user.vo.UserVO;

import javax.validation.constraints.NotBlank;

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
    boolean isUserLoginIdDuplicated(String nickname);

    /**
     * 주어진 이메일과 로그인 타입으로 사용자가 존재하는지 확인합니다.
     *
     * @param email 검색할 사용자의 이메일 주소
     * @param loginType 사용자의 로그인 타입 (예: LOCAL, OAUTH2)
     * @return 해당 이메일과 로그인 타입을 가진 사용자가 존재하면 true, 그렇지 않으면 false
     */
    boolean existsByEmailAndLoginType(String email, LoginType loginType);

    /**
     * 주어진 이메일과 로그인 타입으로 사용자를 찾아 반환
     *
     * @param email 검색할 사용자의 이메일 주소
     * @param loginType 사용자의 로그인 타입 (예: LOCAL, OAUTH2)
     * @return 해당 이메일과 로그인 타입을 가진 사용자가 존재하면 반환, 그렇지 않으면 null
     */
    UserVO findByEmailAndLoginType(@NotBlank(message = "이메일은 필수 입니다.") String email, LoginType loginType);

    /**
     * userId 로 식별된 사용자의 ChoogooMi(선호 타입)를 업데이트합니다.
     *
     * @param userId ChoogooMi를 업데이트할 사용자의 id
     * @param choogooMi 사용자에게 설정할 새로운 ChoogooMi 값
     */
    void updateChoogooMiByUserId(Long userId, ChoogooMi choogooMi);

    /**
     * userId로 식별된 사용자의 닉네임을 업데이트합니다.
     *
     * @param userId nickname을 업데이트할 사용자의 id
     * @param nickname 사용자에게 설정할 새로운 nickname
     */
    void updateUserNicknameByUserId(Long userId, String nickname);

    /**
     * userId로 식별된 사용자의 프로필 사진을 업데이트합니다.
     *
     * @param userId 프로필 사진을 업데이트할 사용자의 id
     * @param profileImageUrl 사용자에게 설정할 새로운 프로필 사진
     */
    void updateProfileImageUrlByUserId(Long userId, String profileImageUrl);


    /**
     * 시스템에 등록된 모든 사용자의 수를 반환합니다.
     *
     * @return 시스템에 등록된 총 사용자 수
     */
    int countAllUsers();

}
