package com.choogoomoneyna.choogoomoneyna_be.matching.service;

import com.choogoomoneyna.choogoomoneyna_be.matching.enums.MatchingStatus;
import com.choogoomoneyna.choogoomoneyna_be.matching.enums.CommonMissionType;
import com.choogoomoneyna.choogoomoneyna_be.matching.enums.MatchingResult;
import com.choogoomoneyna.choogoomoneyna_be.matching.mapper.MatchingMapper;
import com.choogoomoneyna.choogoomoneyna_be.matching.vo.MatchingVO;
import com.choogoomoneyna.choogoomoneyna_be.matching.vo.RoundInfoVO;
import com.choogoomoneyna.choogoomoneyna_be.matching.vo.UserMatchingHistoryVO;
import com.choogoomoneyna.choogoomoneyna_be.score.service.ScoreService;
import com.choogoomoneyna.choogoomoneyna_be.score.vo.UserScoreVO;
import com.choogoomoneyna.choogoomoneyna_be.user.dto.ChoogooMi;
import com.choogoomoneyna.choogoomoneyna_be.user.service.UserService;
import com.choogoomoneyna.choogoomoneyna_be.user.vo.MatchedUserVO;
import com.choogoomoneyna.choogoomoneyna_be.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchingServiceImpl implements MatchingService {

    private final MatchingMapper matchingMapper;
    private final ScoreService scoreService;
    private final RoundInfoService roundInfoService;
    private final MatchingMissionResultService matchingMissionResultService;
    private final UserService userService;
    private final UserMatchingHistoryService userMatchingHistoryService;

    private int roundNumber;

    private void assignMatchMission(Long user1Id, Long user2Id, ChoogooMi choogooMi) {
        Long matchingId = matchingMapper.getProgressMatchingIdByUserId(user1Id);
        if (matchingId == null) {
            throw new IllegalArgumentException("Matching ID is null");
        }

        int commonMissionId = CommonMissionType.COMMON.getRandomId();
        List<Integer> choogooMiMissionIds = choogooMi.getMissionType().getRandomIds(2);

        System.out.println("matchingId: " + matchingId);
        System.out.println("choogooMiMissionIds: " + choogooMiMissionIds);
        System.out.println("commonMissionId: " + commonMissionId);
        System.out.println();

        matchingMissionResultService.createMatchingMissionResult(user1Id, matchingId, commonMissionId);
        if (!user1Id.equals(user2Id)) {
            matchingMissionResultService.createMatchingMissionResult(user2Id, matchingId, commonMissionId);
        }
    }

    private MatchingVO buildToMatchingVO(Long user1Id, Long user2Id) {
        return MatchingVO.builder()
                .user1Id(user1Id)
                .roundNumber(roundNumber)
                .user2Id(user2Id)
                .matchingStatus(MatchingStatus.PROGRESS.name())
                .build();
    }

    /**
     * 점수로 정렬된 유저들을 둘씩 매칭해주는 알고리즘 작성
     */
    private void pairUsersAndSave(List<MatchedUserVO> users, ChoogooMi choogooMi) {

        int userSize = users.size();
        boolean[] isMatched = new boolean[userSize];
        Random random = new Random();

        // 10등 안으로 차이 나게 랜덤 매칭
        for (int i = 0; i < userSize; i++) {
            if (isMatched[i]) {
                continue;
            }

            for (int j = i + random.nextInt(1, 10); j < userSize; j++) {
                if (!isMatched[j]) {
                    isMatched[i] = true;
                    isMatched[j] = true;

                    matchingMapper.insertMatching(buildToMatchingVO(users.get(i).getId(), users.get(j).getId()));
                    assignMatchMission(users.get(i).getId(), users.get(j).getId(), choogooMi);

                    break;
                }
            }
        }

        // 매칭 안된 사람끼리 순서대로
        boolean one = false;
        MatchedUserVO user1 = null;
        for (int i = 0; i < userSize; i++) {
            if (!isMatched[i]) {
                if (one) {
                    matchingMapper.insertMatching(buildToMatchingVO(user1.getId(), users.get(i).getId()));
                    assignMatchMission(user1.getId(), users.get(i).getId(), choogooMi);
                    user1 = null;
                } else {
                    user1 = users.get(i);
                }
                isMatched[i] = true;
                one = !one;
            }
        }

        // 한명이 남음
        if (one) {
            // TODO: dummy data로 넣도록 수정 -> 일단 본인
            matchingMapper.insertMatching(buildToMatchingVO(user1.getId(), user1.getId()));
            assignMatchMission(user1.getId(), user1.getId(), choogooMi);
        }
    }
    
    private List<UserVO> groupByChoogooMi(List<UserVO> users, ChoogooMi choogooMi) {
        return users.stream()
                .filter(user -> user.getChoogooMi().equals(choogooMi.name()))
                .collect(Collectors.toList());
    }

    /**
     * 주어진 날짜로부터 다음주 같은 요일의 날짜를 반환
     *
     * @param date 기준이 되는 날짜
     * @return 다음 주 같은 요일의 날짜
     */
    private Date getNextWeekDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 7);
        return cal.getTime();
    }

    /**
     * 데이터베이스의 라운드 정보를 업데이트합니다. 새로운 라운드를 생성하고 라운드 번호를 증가시키며
     * 시작일과 종료일을 업데이트합니다.
     *
     * <b>처리 과정:</b>
     * 1. roundInfoService를 사용하여 최신 라운드 정보를 가져옵니다.
     * 2. 현재 라운드 번호에 1을 더해 다음 라운드 번호를 계산합니다.
     * 3. 최신 라운드의 시작일과 종료일에 7일을 더해 다음 라운드의 시작일과 종료일을 결정합니다.
     * 4. roundInfoService의 createRoundInfo 메소드를 호출하여 계산된 값으로 
     *    데이터베이스에 새로운 라운드 항목을 생성합니다.
     *
     * <b>사용된 의존성:</b>
     * - roundInfoService: 라운드 정보 데이터와 상호작용하는 서비스 계층
     * - RoundInfoVO: 라운드의 세부 정보를 포함하는 값 객체
     *
     */
    private void prepareNewRoundAndUpdateDates() {
        RoundInfoVO roundInfoVO = roundInfoService.getLatestRoundInfo();
        int nextRoundNumber = roundInfoVO.getRoundNumber() + 1;
        Date nextStartDate = getNextWeekDate(roundInfoVO.getStartDate());
        Date nextEndDate = getNextWeekDate(roundInfoVO.getEndDate());

        roundNumber = nextRoundNumber;

        roundInfoService.createRoundInfo(
                RoundInfoVO.builder()
                        .roundNumber(nextRoundNumber)
                        .startDate(nextStartDate)
                        .endDate(nextEndDate)
                        .build()
        );
    }

    @Override
    public void startAllMatching() {
        prepareNewRoundAndUpdateDates();

        List<UserVO> totalUsers = userService.findAllUsers();
        List<UserScoreVO> scores = scoreService.getAllScores();

        Map<Long, Integer> scoreMap = scores.stream()
                .collect(Collectors.toMap(UserScoreVO::getUserId, UserScoreVO::getScore));

        // User를 점수에 따라 내림차순 정렬
        for (ChoogooMi choogooMi : ChoogooMi.values()) {
            List<UserVO> users = groupByChoogooMi(totalUsers, choogooMi);
            List<MatchedUserVO> matchableUsers = users.stream()
                    .map(user -> new MatchedUserVO(
                            user.getId(),
                            user.getEmail(),
                            user.getProfileImageUrl(),
                            scoreMap.getOrDefault(user.getId(), 0)))
                    .sorted(Comparator.comparingInt(MatchedUserVO::getUserScore).reversed())
                    .toList();

            pairUsersAndSave(matchableUsers, choogooMi);
        }
    }

    @Override
    public void startMatching(Long userId) {
        // TODO: dummy data로 넣도록 수정 -> 일단 본인
        matchingMapper.insertMatching(buildToMatchingVO(userId, userId));
        assignMatchMission(userId, userId, userService.getChoogooMiByUserId(userId));
    }

    /**
     * Inserts the match result into the user matching history.
     *
     * @param userId the unique identifier of the user
     * @param matchingId the unique identifier of the matching session
     * @param roundNumber the round number of the match
     * @param result the result of the matching session
     */
    private void insertUserMatchingHistoryMatchResult(
            long userId, long matchingId, int roundNumber, MatchingResult result
    ) {
        userMatchingHistoryService.insertUserMatchingHistory(
                UserMatchingHistoryVO.builder()
                        .userId(userId)
                        .matchingId(matchingId)
                        .roundNumber(roundNumber)
                        .matchingResult(result.name())
                        .build()
        );
    }

    @Override
    @Transactional
    public void finishAllMatching() {
        // 승리 점수
        int unitScore = 50;
        
        // 진행 중인 매칭 전체 가져오기
        List<MatchingVO> progressMatchings = matchingMapper.findAllProgressMatchings();

        for (MatchingVO progressMatching : progressMatchings) {
            long matchingId = progressMatching.getId();
            int roundNumber = progressMatching.getRoundNumber();

            long user1Id = progressMatching.getUser1Id();
            long user2Id = progressMatching.getUser2Id();

            int user1Score = matchingMissionResultService.getAllScoreByUserIdAndMatchingId(user1Id, matchingId);
            int user2Score = matchingMissionResultService.getAllScoreByUserIdAndMatchingId(user2Id, matchingId);
            
            if (user1Score > user2Score) {
                insertUserMatchingHistoryMatchResult(user1Id, matchingId, roundNumber, MatchingResult.WIN);
                insertUserMatchingHistoryMatchResult(user2Id, matchingId, roundNumber, MatchingResult.LOSE);
                
                user1Score += unitScore;
            } else if (user1Score < user2Score) {
                insertUserMatchingHistoryMatchResult(user1Id, matchingId, roundNumber, MatchingResult.LOSE);
                insertUserMatchingHistoryMatchResult(user2Id, matchingId, roundNumber, MatchingResult.WIN);

                user2Score += unitScore;
            } else {
                insertUserMatchingHistoryMatchResult(user1Id, matchingId, roundNumber, MatchingResult.DRAW);
                insertUserMatchingHistoryMatchResult(user2Id, matchingId, roundNumber, MatchingResult.DRAW);

                user1Score += unitScore / 2;
                user2Score += unitScore / 2;
            }

            int updateScore1 = scoreService.getScore(user1Id) + user1Score;
            scoreService.updateScore(
                    UserScoreVO.builder()
                            .userId(user1Id)
                            .score(updateScore1)
                            .build()
            );

            int updateScore2 = scoreService.getScore(user2Id) + user2Score;
            scoreService.updateScore(
                    UserScoreVO.builder()
                            .userId(user2Id)
                            .score(updateScore2)
                            .build()
            );
        }

        // 매칭 상태가 Progress인 column을 전부 Completed로 변경
        matchingMapper.updateAllProgressMatchings();
    }

    @Override
    public void updateMatchingStatus(Long matchingId, String matchingStatus) {
        MatchingVO match = matchingMapper.findMatchingByMatchingId(matchingId);
        if (match == null) {
            return;
        }

        matchingMapper.updateMatchingStatus(matchingId, matchingStatus);
    }

    @Override
    public String findMatchingStatus(Long matchingId) {
        return matchingMapper.findMatchingStatus(matchingId);
    }

    @Override
    public List<MatchingVO> findRecentNMatchingsByUserId(Long userId, int limit) {
        return matchingMapper.findRecentNMatchingsByUserId(userId, limit);
    }

    @Override
    public List<MatchingVO> findAllMatchingsByUserId(Long userId) {
        return matchingMapper.findAllMatchingsByUserId(userId);
    }

    @Override
    public Long getProgressMatchingIdByUserId(Long userId) {
        return matchingMapper.getProgressMatchingIdByUserId(userId);
    }

    @Override
    public Long getComponentUserIdByUserId(Long userId) {
        return matchingMapper.getComponentUserIdByUserId(userId);
    }
}
