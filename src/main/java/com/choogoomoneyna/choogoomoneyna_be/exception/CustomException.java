package com.choogoomoneyna.choogoomoneyna_be.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final ResponseCode responseCode;

    public CustomException(ResponseCode responseCode) {
        super(responseCode.getMessage());
        this.responseCode = responseCode;
    }

    public CustomException(ResponseCode responseCode, String customMessage) {
        super(customMessage);
        this.responseCode = responseCode;
    }

    public CustomException(ResponseCode responseCode, String customMessage, Throwable cause) {
        super(customMessage, cause);
        this.responseCode = responseCode;
    }
}