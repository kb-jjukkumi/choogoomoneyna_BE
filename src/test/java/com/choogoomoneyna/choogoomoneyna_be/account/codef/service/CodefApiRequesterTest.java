package com.choogoomoneyna.choogoomoneyna_be.account.codef.service;

import com.choogoomoneyna.choogoomoneyna_be.account.codef.dto.AccountRequestDto;
import com.choogoomoneyna.choogoomoneyna_be.account.codef.dto.AccountResponseDto;
import com.choogoomoneyna.choogoomoneyna_be.account.codef.mapper.AccountMapper;
import com.choogoomoneyna.choogoomoneyna_be.account.codef.vo.AccountVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CodefApiRequesterTest {

    private CodefTokenManager codefTokenManager;
    private AccountMapper accountMapper;

    @BeforeEach
    void setUp() {
        codefTokenManager = mock(CodefTokenManager.class);
        accountMapper = mock(AccountMapper.class);
    }

    @Test
    void getAccountInfo_신규계좌만리턴() throws Exception {
        // given
        String dummyConnectedId = "dummy-connected-id";
        AccountRequestDto accountRequestDto = new AccountRequestDto();
        accountRequestDto.setBankId("020");

        // codefTokenManager mock
        when(codefTokenManager.getAccessToken()).thenReturn("dummy-access-token");

        // sendPostRequest mock 응답 JSON
        String mockApiResponse = """
            {
              "result": { "message": "success" },
              "data": {
                "resDepositTrust": [
                  {
                    "resAccount": "1234567890",
                    "resAccountBalance": "100000",
                    "resAccountName": "입출금계좌"
                  },
                  {
                    "resAccount": "1111222233",
                    "resAccountBalance": "200000",
                    "resAccountName": "비상금계좌"
                  }
                ]
              }
            }
            """;

        // sendPostRequest를 mock 처리하기 위해 spy 객체 사용
        CodefApiRequester spyRequester = Mockito.spy(new CodefApiRequester(codefTokenManager, null, accountMapper));
        doReturn(mockApiResponse).when(spyRequester).sendPostRequest(anyString(), anyString(), anyString());

        // accountMapper가 1개 계좌만 DB에 있다고 가정 (하나는 중복임)
        when(accountMapper.findAccountByAccountNum("1234567890")).thenReturn(null); // 신규 계좌
        when(accountMapper.findAccountByAccountNum("1111222233")).thenReturn(new AccountVO()); // 기존 계좌

        // when
        List<AccountResponseDto> result = spyRequester.getAccountList(accountRequestDto, dummyConnectedId);

        // then
        assertEquals(1, result.size());
        assertEquals("1234567890", result.get(0).getAccountNum());
        assertEquals("입출금계좌", result.get(0).getAccountName());
        assertEquals("100000", result.get(0).getAccountBalance());
        assertEquals("020", result.get(0).getBankId());
    }
}
