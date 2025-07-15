package com.choogoomoneyna.choogoomoneyna_be.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = {
        "com.choogoomoneyna.choogoomoneyna_be.user.mapper"
})
public class MyBatisConfig {
}
