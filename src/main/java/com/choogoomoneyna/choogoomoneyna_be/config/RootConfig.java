package com.choogoomoneyna.choogoomoneyna_be.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@ComponentScan(basePackages = {
        "com.choogoomoneyna.choogoomoneyna_be"
})
@Configuration
@Import(MyBatisConfig.class)
@EnableScheduling
public class RootConfig {
}
