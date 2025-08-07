package com.choogoomoneyna.choogoomoneyna_be.exception;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
        super(message);
    }
}
