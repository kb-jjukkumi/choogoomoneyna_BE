package com.choogoomoneyna.choogoomoneyna_be.auth.jwt.util;

import com.choogoomoneyna.choogoomoneyna_be.exception.InvalidTokenException;
import com.choogoomoneyna.choogoomoneyna_be.user.service.CustomUserDetailsService;
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
    private final CustomUserDetailsService customUserDetailsService;

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

        // HTTP 요청 헤더에서 Authorization 헤더 값 가져오기
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        try {
            // Authorization 헤더가 없거나 "Bearer "로 시작하지 않으면 예외 발생
            if (bearerToken == null || !bearerToken.startsWith(BEARER_PREFIX)) {
                SecurityContextHolder.clearContext();
                throw new InvalidTokenException("Invalid bearer token");
            }

            // "Bearer " 접두어 제거하여 실제 토큰 부분만 추출
            String token = bearerToken.substring(BEARER_PREFIX.length());

            // 토큰이 유효하지 않거나 만료되었으면 예외 발생
            if (!jwtTokenProvider.validateToken(token)) {
                throw new InvalidTokenException("Invalid or expired token");
            }

            // 토큰으로부터 인증 정보를 생성하여 SecurityContext에 저장 (인증 처리 완료)
            Authentication authentication = getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
        } catch (InvalidTokenException ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            String json = String.format("{\"message\":\"%s\"}", ex.getMessage());
            response.getWriter().write(json);
        }
    }

    /**
     * JWT 토큰으로부터 인증 객체를 생성합니다.
     *
     * @param token 사용자 정보를 추출할 JWT 토큰
     * @return 사용자 상세 정보가 포함된 인증 객체
     */
    private Authentication getAuthentication(String token) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token);

        CustomUserDetails userDetails =
                (CustomUserDetails) customUserDetailsService.loadUserByUsername(String.valueOf(userId));

        return new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
    }
}
