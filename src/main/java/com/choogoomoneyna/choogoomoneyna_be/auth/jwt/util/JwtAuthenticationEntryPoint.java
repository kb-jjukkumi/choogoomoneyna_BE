package com.choogoomoneyna.choogoomoneyna_be.auth.jwt.util;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT 인증이 실패했을 때 처리하는 진입점 클래스
 * 인증되지 않은 사용자가 보호된 리소스에 접근하려고 할 때 호출
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {


    /**
     * 인증되지 않은 사용자의 요청을 처리
     *
     * @param request       현재 HTTP 요청
     * @param response      HTTP 응답 객체
     * @param authException 발생한 인증 예외
     * @throws IOException IO 예외가 발생할 경우
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
        throws IOException {
        System.out.println(">>> JwtAuthenticationEntryPoint 발동됨");
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: JWT authentication required.");
        }
}
