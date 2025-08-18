package com.choogoomoneyna.choogoomoneyna_be.user.service;

import java.util.regex.Pattern;

public class PasswordValidator {

    // 비밀번호 정규식 패턴
    private static final String PASSWORD_REGEX = "^[A-Za-z0-9!@#$%^&*()_+{}\\[\\]:;<>,.?~\\\\/-]{8,20}$";

    /**
     * 비밀번호 형식 체크
     * @param password 확인할 비밀번호
     * @return 형식이 맞으면 true, 아니면 false
     */
    public static boolean isValidFormat(String password) {
        if (password == null || password.trim().isEmpty()) {
            return false;
        }
        return Pattern.matches(PASSWORD_REGEX, password);
    }

    /**
     * 비밀번호 일치 여부 체크
     * @param password 비밀번호1
     * @param passwordConfirm 비밀번호2
     * @return 두 비밀번호가 모두 null이 아니고 동일하면 true, 아니면 false
     */
    public static boolean isMatching(String password, String passwordConfirm) {
        if (password == null || passwordConfirm == null) {
            return false;
        }
        return password.equals(passwordConfirm);
    }
}
