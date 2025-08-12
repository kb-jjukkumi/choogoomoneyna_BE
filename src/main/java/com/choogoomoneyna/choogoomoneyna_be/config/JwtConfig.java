package com.choogoomoneyna.choogoomoneyna_be.config;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.properties")
@Getter
@Setter
public class JwtConfig {

    private final String secretKey;
    private final long accessTokenExpirationTime;
    private final long refreshTokenExpirationTime;

    // 생성자에 dotenv 라이브러리를 받아서 직접 값을 읽어 저장
    public JwtConfig(Dotenv dotenv) {
        this.secretKey = dotenv.get("JWT_SECRET");
        // 환경변수가 없으면 기본값(86400000) 넣기
        this.accessTokenExpirationTime = Long.parseLong(dotenv.get("JWT_ACCESS_EXPIRATION", "86400000"));
        this.refreshTokenExpirationTime = Long.parseLong(dotenv.get("JWT_REFRESH_EXPIRATION", "8640000000"));
    }

    // 게터는 lombok @Getter 써도 되고 직접 만들면 되고요
}
