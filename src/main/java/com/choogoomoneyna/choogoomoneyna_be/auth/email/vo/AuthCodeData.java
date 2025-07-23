package com.choogoomoneyna.choogoomoneyna_be.auth.email.vo;

import lombok.Getter;

/**
 * @param expireAt timestamp in millis
 */
public record AuthCodeData(String code, long expireAt) {

    public boolean isExpired() {
        return System.currentTimeMillis() > expireAt;
    }
}

