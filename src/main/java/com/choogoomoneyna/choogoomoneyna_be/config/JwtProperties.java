package com.choogoomoneyna.choogoomoneyna_be.config;

import org.springframework.beans.factory.annotation.Value;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.properties")
@Getter
@Setter
public class JwtProperties {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private long accessTokenExpirationTime;

    @Value("${jwt.refresh.expiration}")
    private long refreshTokenExpirationTime;
}
