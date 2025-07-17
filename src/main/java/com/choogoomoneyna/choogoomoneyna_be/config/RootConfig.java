package com.choogoomoneyna.choogoomoneyna_be.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(MyBatisConfig.class)
public class RootConfig {
}
