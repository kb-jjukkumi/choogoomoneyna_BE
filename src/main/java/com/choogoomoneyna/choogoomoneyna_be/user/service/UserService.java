package com.choogoomoneyna.choogoomoneyna_be.user.service;

import com.choogoomoneyna.choogoomoneyna_be.user.enums.ChoogooMi;
import com.choogoomoneyna.choogoomoneyna_be.user.enums.LoginType;
import com.choogoomoneyna.choogoomoneyna_be.user.vo.UserVO;

import javax.validation.constraints.NotBlank;
import java.util.List;

public interface UserService {

    /**
     * 새로운 사용자를 시스템에 등록합니다.
     *
     * @param user 등록할 사용자의 상세 정보가 담긴 UserVO 객체
     */
    void insertUser(UserVO user);

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
     * @param email     검색할 사용자의 이메일 주소
     * @param loginType 사용자의 로그인 타입 (예: LOCAL, OAUTH2)
     * @return 해당 이메일과 로그인 타입을 가진 사용자가 존재하면 true, 그렇지 않으면 false
     */
    boolean existsByEmailAndLoginType(String email, LoginType loginType);

    /**
     * 주어진 이메일과 로그인 타입으로 사용자를 찾아 반환
     *
     * @param email     검색할 사용자의 이메일 주소
     * @param loginType 사용자의 로그인 타입 (예: LOCAL, OAUTH2)
     * @return 해당 이메일과 로그인 타입을 가진 사용자가 존재하면 반환, 그렇지 않으면 null
     */
    UserVO findByEmailAndLoginType(@NotBlank(message = "이메일은 필수 입니다.") String email, LoginType loginType);

    /**
     * userId로 식별된 사용자의 닉네임과 비밀번호를 업데이트합니다.
     *
     * @param userId   업데이트할 사용자의 Id
     * @param nickname 사용자에게 설정할 새로운 닉네임
     * @param password 사용자에게 설정할 새로운 비밀번호
     */
    void updateNicknameAndPasswordByUserId(Long userId, String nickname, String password);

    /**
     * userId 로 식별된 사용자의 ChoogooMi(선호 타입)를 업데이트합니다.
     *
     * @param userId    ChoogooMi를 업데이트할 사용자의 id
     * @param choogooMi 사용자에게 설정할 새로운 ChoogooMi 값
     */
    void updateChoogooMiByUserId(Long userId, ChoogooMi choogooMi);

    /**
     * userId로 식별된 사용자의 닉네임을 업데이트합니다.
     *
     * @param userId   nickname을 업데이트할 사용자의 id
     * @param nickname 사용자에게 설정할 새로운 nickname
     */
    void updateUserNicknameByUserId(Long userId, String nickname);

    /**
     * userId로 식별된 사용자의 프로필 사진을 업데이트합니다.
     *
     * @param userId          프로필 사진을 업데이트할 사용자의 id
     * @param profileImageUrl 사용자에게 설정할 새로운 프로필 사진
     */
    void updateProfileImageUrlByUserId(Long userId, String profileImageUrl);


    /**
     * 주어진 이메일로 식별된 사용자의 비밀번호를 업데이트합니다.
     *
     * @param email    비밀번호를 업데이트할 사용자의 이메일 주소
     * @param password 사용자에게 설정할 새로운 비밀번호
     */
    void updatePasswordByUserEmail(String email, String password);

    /**
     * 시스템에 등록된 모든 사용자의 수를 반환합니다.
     *
     * @return 시스템에 등록된 총 사용자 수
     */
    int countAllUsers();

    /**
     * 주어진 사용자 ID에 해당하는 ChoogooMi(선호 타입)를 조회합니다.
     *
     * @param userId 조회할 사용자의 ID
     * @return 사용자의 ChoogooMi(선호 타입) 값
     */
    ChoogooMi getChoogooMiByUserId(Long userId);

    /**
     * 주어진 사용자 ID를 기준으로 사용자의 닉네임을 조회합니다.
     *
     * @param userId 닉네임을 조회할 사용자의 고유 식별자
     * @return 사용자의 닉네임 (문자열), 사용자를 찾을 수 없는 경우 null 반환
     */
    String getNicknameByUserId(Long userId);

    /**
     * 시스템에 등록된 모든 사용자 목록을 조회합니다.
     *
     * @return 시스템에 등록된 모든 사용자 목록
     */
    List<UserVO> findAllUsers();
}