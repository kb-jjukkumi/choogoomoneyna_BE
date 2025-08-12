package com.choogoomoneyna.choogoomoneyna_be.config;

import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.scheduling.annotation.EnableScheduling;

@ComponentScan(basePackages = {
        "com.choogoomoneyna.choogoomoneyna_be.account.codef.service",
        "com.choogoomoneyna.choogoomoneyna_be.account.db.service",
        "com.choogoomoneyna.choogoomoneyna_be.auth.email.service",
        "com.choogoomoneyna.choogoomoneyna_be.auth.jwt.service",
        "com.choogoomoneyna.choogoomoneyna_be.auth.oauth.service",
        "com.choogoomoneyna.choogoomoneyna_be.batch",
        "com.choogoomoneyna.choogoomoneyna_be.config",
        "com.choogoomoneyna.choogoomoneyna_be.exception",
        "com.choogoomoneyna.choogoomoneyna_be.matching.service",
        "com.choogoomoneyna.choogoomoneyna_be.mission.service",
        "com.choogoomoneyna.choogoomoneyna_be.mock.service",
        "com.choogoomoneyna.choogoomoneyna_be.ranking.service",
        "com.choogoomoneyna.choogoomoneyna_be.report.service",
        "com.choogoomoneyna.choogoomoneyna_be.score.service",
        "com.choogoomoneyna.choogoomoneyna_be.survey.service",
        "com.choogoomoneyna.choogoomoneyna_be.user.service",
        "com.choogoomoneyna.choogoomoneyna_be.auth.jwt.util",

})
@Configuration
@Import(MyBatisConfig.class)
@EnableScheduling
class RootConfig {

    @Configuration
    @Profile("local")
    @PropertySource("classpath:application-local.properties")
    static class LocalProperties {
        @Bean
        public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
            return new PropertySourcesPlaceholderConfigurer();
        }
    }

    @Configuration
    @Profile("prod")
    @PropertySource("classpath:application-prod.properties")
    static class ProdProperties {
        @Bean
        public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
            return new PropertySourcesPlaceholderConfigurer();
        }
    }
}
