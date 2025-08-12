package com.choogoomoneyna.choogoomoneyna_be.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;

public class EnvLoader implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        String dotenvDir = System.getenv("DOTENV_DIR");
        if (dotenvDir == null || dotenvDir.isEmpty()) {
            dotenvDir = "C:\\choogoomoneyna\\choogoomoneyna_be";
        }
        System.out.println("Dotenv directory: " + dotenvDir);

        Dotenv dotenv = Dotenv.configure()
                .directory(dotenvDir)
                .filename(".env")
                .load();

        String springProfile = dotenv.get("SPRING_PROFILES_ACTIVE");
        if (springProfile != null && System.getProperty("spring.profiles.active") == null) {
            System.setProperty("spring.profiles.active", springProfile);
        }

        dotenv.entries().forEach(entry -> {
            if (System.getProperty(entry.getKey()) == null) {
                System.setProperty(entry.getKey(), entry.getValue());
            }
        });

        System.out.println("Active profiles: " + Arrays.toString(applicationContext.getEnvironment().getActiveProfiles()));
    }
}
