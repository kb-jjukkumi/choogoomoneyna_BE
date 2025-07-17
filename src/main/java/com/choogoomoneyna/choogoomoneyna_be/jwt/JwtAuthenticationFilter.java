package com.choogoomoneyna.choogoomoneyna_be.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

/**
 * HTTP 요청의 JWT 토큰 인증을 처리하는 필터.
 * Authorization 헤더에서 JWT 토큰을 추출하고 검증한 후,
 * 유효한 토큰인 경우 SecurityContext에 인증 정보를 설정합니다.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Authorization 헤더의 JWT 토큰을 사용하여 HTTP 요청을 인증 처리합니다.
     *
     * @param request     처리할 HTTP 요청
     * @param response     HTTP 응답
     * @param filterChain 실행할 필터 체인
     * @throws ServletException 서블릿 처리 중 오류 발생시
     * @throws IOException      입출력 처리 중 오류 발생시
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
    throws ServletException, IOException {
        String path = request.getRequestURI();
        System.out.println(">>> JwtAuthenticationFilter path: " + path);
        if (path.contains("/login") || path.contains("/signup")) {
            filterChain.doFilter(request, response);
            return;
        }

        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
            String token = bearerToken.substring(BEARER_PREFIX.length());

            // 유효한 토큰이면 인증 객체 생성 후 SecurityContext에 설정
            if (jwtTokenProvider.validateToken(token)) {
                Authentication authentication = getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * JWT 토큰으로부터 인증 객체를 생성합니다.
     *
     * @param token 사용자 정보를 추출할 JWT 토큰
     * @return 사용자 상세 정보가 포함된 인증 객체
     */
    private Authentication getAuthentication(String token) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        String nickname = jwtTokenProvider.getNicknameFromToken(token);

        Map<String, Object> principal = Map.of(
                "userId", userId,
                "nickname", nickname
        );
        return new UsernamePasswordAuthenticationToken(principal, null, Collections.emptyList());
    }


}
