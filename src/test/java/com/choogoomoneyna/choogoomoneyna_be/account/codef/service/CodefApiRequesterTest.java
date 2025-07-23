package com.choogoomoneyna.choogoomoneyna_be.account.codef.service;

import com.choogoomoneyna.choogoomoneyna_be.account.codef.dto.AccountRequestDto;
import com.choogoomoneyna.choogoomoneyna_be.account.codef.dto.AccountResponseDto;
import com.choogoomoneyna.choogoomoneyna_be.account.codef.dto.TransactionRequestDto;
import com.choogoomoneyna.choogoomoneyna_be.account.codef.dto.TransactionResponseDto;
import com.choogoomoneyna.choogoomoneyna_be.account.db.mapper.AccountMapper;
import com.choogoomoneyna.choogoomoneyna_be.account.codef.vo.AccountVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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
    void getAccountList() throws Exception {
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
        when(accountMapper.findByAccountNum("1234567890")).thenReturn(null); // 신규 계좌
        when(accountMapper.findByAccountNum("1111222233")).thenReturn(new AccountVO()); // 기존 계좌

        // when
        List<AccountResponseDto> result = spyRequester.getAccountList(accountRequestDto, dummyConnectedId);

        // then
        assertEquals(1, result.size());
        assertEquals("1234567890", result.get(0).getAccountNum());
        assertEquals("입출금계좌", result.get(0).getAccountName());
        assertEquals("100000", result.get(0).getAccountBalance());
        assertEquals("020", result.get(0).getBankId());
    }

//    @Test
//    void getTransactionList() throws Exception {
//        // given
//        String dummyConnectedId = "dummy-connected-id";
//        TransactionRequestDto requestDto = new TransactionRequestDto();
//        requestDto.setAccountNum("12345678901234");
//        requestDto.setBankId("020");
//        requestDto.setStartDate("20250701");
//        requestDto.setEndDate("20250721");
//
//        // 더미 응답 JSON
//        String mockApiResponse = """
//            {
//              "result": {
//                "code": "CF-00000",
//                "message": "정상 처리되었습니다"
//              },
//              "data": {
//                "resAccount": "12345678901234",
//                "resTrHistoryList": [
//                  {
//                    "transactionId": 1,
//                    "trDate": "2025-07-15",
//                    "trTime": 153000,
//                    "trAccountOut": 50000,
//                    "trAccountIn": 0,
//                    "trAfterBalance": 950000,
//                    "trDesc1": "홍길동",
//                    "trDesc2": "카페결제",
//                    "trDesc3": "입금",
//                    "trDesc4": "강남지점"
//                  }
//                ]
//              }
//            }
//        """;
//
//        CodefTokenManager mockTokenManager = mock(CodefTokenManager.class);
//        when(mockTokenManager.getAccessToken()).thenReturn("dummy-access-token");
//
//        CodefApiRequester spyRequester = Mockito.spy(new CodefApiRequester(mockTokenManager, null, null));
//        doReturn(mockApiResponse).when(spyRequester)
//                .sendPostRequest(anyString(), anyString(), anyString());
//
//        // when
//        TransactionResponseDto responseDto = spyRequester.getTransactionList(requestDto, dummyConnectedId);
//
//        // then
//        assertNotNull(responseDto);
//        assertEquals("12345678901234", responseDto.getAccountNum());
//        assertEquals(1, responseDto.getTransactionList().size());
//
//        TransactionResponseDto.trItem tr = responseDto.getTransactionList().get(0);
//        assertEquals("2025-07-15", tr.getTrDate());
//        assertEquals(153000, tr.getTrTime());
//        assertEquals(50000, tr.getTrAccountOut());
//        assertEquals("홍길동", tr.getTrDesc1());
//    }
}
