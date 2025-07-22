package com.choogoomoneyna.choogoomoneyna_be.account.codef.service;

import com.choogoomoneyna.choogoomoneyna_be.account.codef.mapper.CodefTokenMapper;
import com.choogoomoneyna.choogoomoneyna_be.account.codef.vo.CodefTokenVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CodefTokenManagerTest {

    private CodefTokenMapper mockMapper;
    private CodefTokenManager realManager;

    @BeforeEach
    void setUp() {
        mockMapper = mock(CodefTokenMapper.class);
        realManager = new CodefTokenManager(mockMapper);
        ReflectionTestUtils.setField(realManager, "clientId", "dummy-client-id");
        ReflectionTestUtils.setField(realManager, "clientSecret", "dummy-client-secret");
    }

    @Test
    void getToken() {
        System.out.println("✅ clientId: " + realManager.clientId);
        System.out.println("✅ clientSecret: " + realManager.clientSecret);
    }
    /**
     * 유효한 토큰이 DB에 존재하고, 만료되지 않았을 때 그대로 반환하는지 테스트
     */
    @Test
    void getAccessToken() {
        // given
        long futureTime = System.currentTimeMillis() + 1000 * 60 * 60;
        CodefTokenVO validToken = new CodefTokenVO();
        validToken.setAccessToken("valid-access-token");
        validToken.setTokenExpiryTime(new Timestamp(futureTime));
        when(mockMapper.getLatestToken()).thenReturn(validToken);

        // 내부 필드도 현재 토큰과 일치하게 설정
        ReflectionTestUtils.setField(realManager, "tokenExpiryTime", futureTime);

        CodefTokenManager spyManager = spy(realManager);
        doNothing().when(spyManager).refreshAccessToken();

        // when
        String accessToken = spyManager.getAccessToken();

        // then
        assertEquals("valid-access-token", accessToken);
        verify(spyManager, never()).refreshAccessToken();
    }

    /**
     * DB에 저장된 토큰이 없을 때 refreshAccessToken()이 호출되는지 테스트
     */
    @Test
    void getAccessToken_토큰없으면_리프레시호출() {
        // given
        when(mockMapper.getLatestToken()).thenReturn(null);
        CodefTokenManager spyManager = spy(realManager);
        doNothing().when(spyManager).refreshAccessToken();

        // when
        spyManager.getAccessToken();

        // then
        verify(spyManager).refreshAccessToken();
    }

    @Test
    void isAccessTokenExpired_토큰기한이지난경우_true반환() {
        // given
        long pastTime = System.currentTimeMillis() - 1000; // 현재보다 과거
        ReflectionTestUtils.setField(realManager, "tokenExpiryTime", pastTime);

        // when
        boolean expired = realManager.isAccessTokenExpired();

        // then
        assertTrue(expired);
    }

    @Test
    void isAccessTokenExpired_토큰기한이남은경우_false반환() {
        // given
        long futureTime = System.currentTimeMillis() + 1000; // 현재보다 미래
        ReflectionTestUtils.setField(realManager, "tokenExpiryTime", futureTime);

        // when
        boolean expired = realManager.isAccessTokenExpired();

        // then
        assertFalse(expired);
    }



}
