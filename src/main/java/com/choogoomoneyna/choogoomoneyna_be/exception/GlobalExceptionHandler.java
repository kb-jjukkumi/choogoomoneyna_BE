package com.choogoomoneyna.choogoomoneyna_be.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.BindException;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<Map<String, String>> buildResponse(ResponseCode code, String message) {
        return ResponseEntity.status(code.getHttpStatus())
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(Map.of(
                        "code", code.name(),
                        "message", message
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        StringBuilder sb = new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            sb.append(error.getField())
                    .append(": ")
                    .append(error.getDefaultMessage())
                    .append("; ");
        });
        return buildResponse(ResponseCode.AUTH_VALIDATION_ERROR, sb.toString());
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<?> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        // 메시지를 Enum 메시지 대신 예외 메시지로 주고 싶으면 ex.getMessage() 사용 가능
        return buildResponse(ResponseCode.AUTH_EMAIL_EXISTS, ex.getMessage());
    }

    @ExceptionHandler(BindException.class)
    @ResponseBody
    public ResponseEntity<?> handleBindException(BindException ex) {
        return buildResponse(ResponseCode.BIND_ERROR, "BindException: " + ex.getMessage());
    }

    @ExceptionHandler(CustomNotFoundException.class)
    public ResponseEntity<?> handleNotFound(CustomNotFoundException ex) {
        return buildResponse(ResponseCode.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<?> tokenNotFound(InvalidTokenException ex) {
        return buildResponse(ResponseCode.AUTH_TOKEN_INVALID, ex.getMessage());
    }

    @ExceptionHandler(OAuthException.class)
    public ResponseEntity<?> handleOAuthException(OAuthException ex) {
        return buildResponse(ResponseCode.OAUTH_FAILURE, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<?> handleGeneric(Exception ex) {
        // 내부 메시지 노출 안 하고 enum 메시지 사용
        return buildResponse(ResponseCode.INTERNAL_SERVER_ERROR, ResponseCode.INTERNAL_SERVER_ERROR.getMessage());
    }
}

