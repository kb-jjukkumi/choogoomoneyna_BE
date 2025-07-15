package com.choogoomoneyna.choogoomoneyna_be.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
// import org.springframework.beans.factory.annotation.Value;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@Component
@Getter
public class JwtProperties {

//    @Value("${jwt.secret-key}")
//    private String secretKey;
//
//    @Value("${jwt.expiration-time}")
//    private long accessTokenExpirationTime;

//    @Value("${jwt.refresh.expiration}")
//    private refreshTokenExpirationTime;
    
    // TODO: application-secret-jwt.properties 와 연결하여 다시 작성할 것
    private String secretKey = "very_very_important_secret_key";
    private long accessTokenExpirationTime = (long)6e10;  // 10분
    private long refreshTokenExpirationTime = (long) 6e12;
}
