package com.choogoomoneyna.choogoomoneyna_be.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    // 400 BadRequest
    AUTH_VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "사용자 입력값이 올바르지 않습니다."),
    AUTH_EMAIL_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다."),
    BIND_ERROR(HttpStatus.BAD_REQUEST, "입력 값 바인딩 오류가 발생했습니다."),

    // 401 Unauthorized
    AUTH_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "유효하지 않거나 만료된 토큰입니다."),
    OAUTH_FAILURE(HttpStatus.UNAUTHORIZED, "OAuth 인증에 실패했습니다."),

    // 404 Not Found
    NOT_FOUND(HttpStatus.NOT_FOUND, "요청하신 리소스를 찾을 수 없습니다."),

    // 500 Internal Server Error
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다."),

    SUCCESS(HttpStatus.OK, "성공했습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
