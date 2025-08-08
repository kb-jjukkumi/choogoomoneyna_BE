package com.choogoomoneyna.choogoomoneyna_be.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@Import(MyBatisConfig.class)
@EnableScheduling
public class RootConfig {
}
