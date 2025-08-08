package com.choogoomoneyna.choogoomoneyna_be.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@ComponentScan(basePackages = {
        "com.choogoomoneyna.choogoomoneyna_be.account.codef.controller",
        "com.choogoomoneyna.choogoomoneyna_be.account.db.controller",
        "com.choogoomoneyna.choogoomoneyna_be.auth.email.controller",
        "com.choogoomoneyna.choogoomoneyna_be.auth.jwt.controller",
        "com.choogoomoneyna.choogoomoneyna_be.auth.oauth.controller",
        "com.choogoomoneyna.choogoomoneyna_be.matching.controller",
        "com.choogoomoneyna.choogoomoneyna_be.mock.controller",
        "com.choogoomoneyna.choogoomoneyna_be.ranking.controller",
        "com.choogoomoneyna.choogoomoneyna_be.report.controller",
        "com.choogoomoneyna.choogoomoneyna_be.survey.controller",
        "com.choogoomoneyna.choogoomoneyna_be.user.controller",
})
@Configuration
@Import(MyBatisConfig.class)
@EnableScheduling
public class RootConfig {
}
