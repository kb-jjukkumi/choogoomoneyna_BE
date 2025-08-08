package com.choogoomoneyna.choogoomoneyna_be.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())  // 📌 문서 정보 설정
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.choogoomoneyna.choogoomoneyna_be"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("추구머니나 API 문서")
                .description("추구머니나 서비스의 Swagger 기반 API 문서입니다.")
                .version("1.0")
                .build();
    }
}