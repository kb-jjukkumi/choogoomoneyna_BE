package com.choogoomoneyna.choogoomoneyna_be.account.db.service;

import com.choogoomoneyna.choogoomoneyna_be.account.codef.dto.AccountResponseDto;
import com.choogoomoneyna.choogoomoneyna_be.account.codef.dto.TransactionResponseDto;
import com.choogoomoneyna.choogoomoneyna_be.account.codef.vo.TransactionVO;
import com.choogoomoneyna.choogoomoneyna_be.account.db.dto.TransactionItemDto;
import com.choogoomoneyna.choogoomoneyna_be.account.db.mapper.AccountMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // ✅ 꼭 필요함!
class AccountDbServiceTest {

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private AccountDbServiceImpl accountDbService;

    @Test
    void getAllAccounts() {
        //given
        Long userId = 1L;
        List<AccountResponseDto> mockAccounts = List.of(
                new AccountResponseDto("1234567890", "KB은행", "0004", "500"),
                new AccountResponseDto("9876543210", "NH은행", "0005", "1000")
        );

        when(accountMapper.findByUserId(userId)).thenReturn(mockAccounts);

        //when
        List<AccountResponseDto> accounts = accountDbService.getAllAccounts(userId);

        //then
        assertNotNull(accounts);
        assertEquals(mockAccounts.size(), accounts.size());
    }

    @Test
    void getAllTransactions() {
        //given
        String account = "1234567890";
        TransactionVO vo1 = TransactionVO.builder()
                .transactionId(1L)
                .accountNum("42330204195039")
                .trTime(LocalDateTime.of(2025, 7, 23, 13, 34, 25))
                .trAccountOut(5000)
                .trAccountIn(0)
                .transactionType("출금")
                .trAfterBalance(127770)
                .trDesc1("신한카드")
                .trDesc2("맥도날드 구로점")
                .trDesc3("카드결제")
                .trDesc4("신한카드")
                .build();

        TransactionVO vo2 = TransactionVO.builder()
                .transactionId(2L)
                .accountNum("42330204195039")
                .trTime(LocalDateTime.of(2025, 7, 22, 9, 15, 0))
                .trAccountOut(0)
                .trAccountIn(300000)
                .transactionType("입금")
                .trAfterBalance(132770)
                .trDesc1("국민연금공단")
                .trDesc2("연금입금")
                .trDesc3("연금")
                .trDesc4("국민연금")
                .build();

        List<TransactionVO> mockVoList = List.of(vo1,vo2);
        when(accountMapper.findAllTransactionsVo(account)).thenReturn(mockVoList);

        //when
        List<TransactionItemDto> dto = accountDbService.getAllTransactions(account);

        //then
        assertNotNull(dto);
        assertEquals(mockVoList.size(), dto.size());

    }
}