package com.choogoomoneyna.choogoomoneyna_be.auth.email.util;

public class AuthCodeGenerator {
    /**
     * 6자리 숫자 인증 코드 생성
     * 예: "532714"
     */
    public static String generate() {
        int code = (int)(Math.random() * 900000) + 100000;
        return String.valueOf(code);
    }
}
