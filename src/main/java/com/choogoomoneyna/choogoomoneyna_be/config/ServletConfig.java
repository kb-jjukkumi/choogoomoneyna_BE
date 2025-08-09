package com.choogoomoneyna.choogoomoneyna_be.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;

@Configuration
@EnableWebMvc
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
        "com.choogoomoneyna.choogoomoneyna_be.dockertest.controller",
})
public class ServletConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");

        registry.addResourceHandler("/resources/**")
                .addResourceLocations("/resources/");
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
}