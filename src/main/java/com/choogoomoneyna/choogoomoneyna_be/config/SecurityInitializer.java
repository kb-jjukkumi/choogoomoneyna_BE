package com.choogoomoneyna.choogoomoneyna_be.config;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * SecurityInitializer 클래스는 애플리케이션에서 Spring Security 설정을 초기화하는 역할을 함
 *
 * 이 클래스는 AbstractSecurityWebApplicationInitializer를 상속받아 사용하며,
 * 이를 통해 Spring Security의 필터 체인(springSecurityFilterChain)을 자동으로 등록
 *
 * 이 클래스를 사용하면 web.xml에서 보안 필터를 따로 설정할 필요 없이,
 * Java 기반 설정(SecurityConfig 등)만으로도 보안 기능을 통합 가능함
 *
 * <p>
 *      애플리케이션이 시작될 때 Spring Security의 설정이 웹 애플리케이션 컨텍스트에 자동으로 적용해주는 역할을 하는 class
 * </p>
 */
public class SecurityInitializer extends AbstractSecurityWebApplicationInitializer {
}
