package com.choogoomoneyna.choogoomoneyna_be.matching.service;

import com.choogoomoneyna.choogoomoneyna_be.account.codef.dto.AccountResponseDto;
import com.choogoomoneyna.choogoomoneyna_be.account.codef.dto.TransactionRequestDto;
import com.choogoomoneyna.choogoomoneyna_be.account.codef.service.CodefService;
import com.choogoomoneyna.choogoomoneyna_be.account.codef.vo.AccountVO;
import com.choogoomoneyna.choogoomoneyna_be.account.db.dto.TransactionItemDto;
import com.choogoomoneyna.choogoomoneyna_be.account.db.service.AccountDbService;
import com.choogoomoneyna.choogoomoneyna_be.matching.mapper.MatchingMissionResultMapper;
import com.choogoomoneyna.choogoomoneyna_be.matching.vo.MatchingMissionResultVO;
import com.choogoomoneyna.choogoomoneyna_be.mission.dto.response.MissionProgressDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchingMissionResultServiceImpl implements MatchingMissionResultService {

    private final MatchingMissionResultMapper matchingMissionResultMapper;
    private final AccountDbService accountDbService;
    private final CodefService codefService;

    @Override
    public void createMatchingMissionResult(Long userId, Long matchingId, Integer missionId) {
        MatchingMissionResultVO vo = MatchingMissionResultVO.builder()
                .userId(userId)
                .matchingId(matchingId)
                .missionId(missionId)
                .resultScore(0)
                .build();

        matchingMissionResultMapper.insertOne(vo);
    }

    @Override
    public void updateMatchingMissionResult(Long userId, Long matchingId, Integer missionId, Integer missionScore) {
        MatchingMissionResultVO existingVO = matchingMissionResultMapper
                .findMatchingMissionResultByUserIdAndMatchingIdAndMissionId(userId, matchingId, missionId);

        if (existingVO == null) return;

        existingVO.setResultScore(existingVO.getResultScore() + missionScore);
        matchingMissionResultMapper.updateOne(existingVO);
    }

    @Override
    public void updateAllResults() {
        // TODO: 구현 예정
    }

    @Override
    public int getAllScoreByUserIdAndMatchingId(Long userId, Long matchingId) {
        return matchingMissionResultMapper.getAllScoreByUserIdAndMatchingId(userId, matchingId);
    }

    @Override
    public List<MatchingMissionResultVO> getMatchingMissionResults(Long userId, Long matchId) {
        return matchingMissionResultMapper.findAllMatchingMissionResultByUserIdAndMatchingId(userId, matchId);
    }

    @Override
    public List<MissionProgressDTO> getAllMissionProgressDTO(Long userId, Long matchId) {
        return matchingMissionResultMapper.getAllUserMissionByUserIdAndMatchingId(userId, matchId)
                .stream()
                .map(MatchingMissionConverter::toMissionProgressDTO)
                .toList();
    }

    @Override
    public void validateMissionType1(Long userId, Long matchingId, Integer missionId, Integer missionSocre, Integer limitAmount) {
        //0. 날짜 계산
        LocalDate today = LocalDate.now().minusDays(1);

        LocalDate startDate = today.with(DayOfWeek.MONDAY);
        LocalDate endDate = today.with(DayOfWeek.SUNDAY);

        String start = startDate.toString();
        String end = endDate.toString();

        //1. 사용자의 지출 합계 필드 정의
        int spent = 0;

        //2. 사용자의 계좌목록 조회
        List<AccountResponseDto> accounts = accountDbService.getAllAccounts(userId);

        //3. 계좌별 for문
        for (AccountResponseDto account : accounts) {
            //4. codef로부터 거래내역 업데이트
            try {
                TransactionRequestDto dto = new TransactionRequestDto();
                dto.setAccount(account.getAccountNum());
                dto.setOrganization(accountDbService.getBankId(account.getAccountNum()));
                dto.setStartDate(start);
                dto.setEndDate(end);
                codefService.addTransaction(userId,dto);
            } catch (Exception e) {
                log.error(e.getMessage(),e);
            }

            //5. 위클리 거래내역 조회
            List<TransactionItemDto> transactions = accountDbService.getWeeklyTransactions(account.getAccountNum(),start,end);
            for (TransactionItemDto transaction : transactions) {
                spent += transaction.getTrAccountOut();
            }
        }

        //6. 지출 총합 비교
        if(spent >= limitAmount) {
            log.info("validate logic finished-- mission failed");
        } else {
            updateMatchingMissionResult(userId, matchingId, missionId, spent);
            log.info("validate logic finished-- mission success{}",spent);
        }
    }
}
