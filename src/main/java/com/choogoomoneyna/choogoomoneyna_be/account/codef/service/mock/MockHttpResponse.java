package com.choogoomoneyna.choogoomoneyna_be.account.codef.service.mock;

import java.net.http.HttpClient;
import java.net.http.HttpHeaders;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

public class MockHttpResponse implements HttpResponse<String> {
    private final String body;
    private final int statusCode;

    public MockHttpResponse(String body, int statusCode) {
        this.body = body;
        this.statusCode = statusCode;
    }

    @Override
    public int statusCode() {
        return statusCode;
    }

    @Override
    public String body() {
        return body;
    }

    // 나머지는 기본값이나 생략 가능한 값들입니다.
    @Override
    public HttpRequest request() {
        return null;
    }

    @Override
    public Optional<HttpResponse<String>> previousResponse() {
        return Optional.empty();
    }

    @Override
    public HttpHeaders headers() {
        return HttpHeaders.of(
                java.util.Map.of(),  // 빈 헤더 맵
                (k, v) -> true        // 모든 키-값 허용
        );
    }

    @Override
    public HttpClient.Version version() {
        return HttpClient.Version.HTTP_1_1;  // 또는 null도 가능
    }


    @Override
    public URI uri() {
        return null;
    }

    @Override
    public Optional<javax.net.ssl.SSLSession> sslSession() {
        return Optional.empty();
    }
}
