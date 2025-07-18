package com.choogoomoneyna.choogoomoneyna_be.auth.email.service;

public interface EmailAuthService {
    void sendAuthCode(String email);
    boolean verifyAuthCode(String email, String code);
}
