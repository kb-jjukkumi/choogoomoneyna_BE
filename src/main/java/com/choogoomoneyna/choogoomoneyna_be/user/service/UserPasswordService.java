package com.choogoomoneyna.choogoomoneyna_be.user.service;

public interface UserPasswordService {

    /**
     * 제공된 이메일로 식별된 사용자의 비밀번호를 재설정합니다.
     *
     * @param email              비밀번호를 재설정할 사용자의 이메일 주소
     * @param newPassword        사용자에게 설정할 새로운 비밀번호
     * @param newPasswordConfirm 새로운 비밀번호 일치 여부를 확인하기 위한 비밀번호 확인
     */
    void resetPassword(String email, String newPassword, String newPasswordConfirm);

}