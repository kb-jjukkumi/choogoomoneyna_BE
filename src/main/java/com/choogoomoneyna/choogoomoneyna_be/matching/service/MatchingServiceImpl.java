package com.choogoomoneyna.choogoomoneyna_be.matching.service;

import com.choogoomoneyna.choogoomoneyna_be.matching.enums.MatchingStatus;
import com.choogoomoneyna.choogoomoneyna_be.matching.enums.CommonMissionType;
import com.choogoomoneyna.choogoomoneyna_be.matching.enums.MatchingResult;
import com.choogoomoneyna.choogoomoneyna_be.matching.mapper.MatchingMapper;
import com.choogoomoneyna.choogoomoneyna_be.matching.vo.MatchingVO;
import com.choogoomoneyna.choogoomoneyna_be.matching.vo.RoundInfoVO;
import com.choogoomoneyna.choogoomoneyna_be.matching.vo.UserMatchingHistoryVO;
import com.choogoomoneyna.choogoomoneyna_be.ranking.service.RankingService;
import com.choogoomoneyna.choogoomoneyna_be.ranking.service.RankingUpdateService;
import com.choogoomoneyna.choogoomoneyna_be.ranking.vo.RankingVO;
import com.choogoomoneyna.choogoomoneyna_be.score.service.ScoreCalculateService;
import com.choogoomoneyna.choogoomoneyna_be.score.service.ScoreService;
import com.choogoomoneyna.choogoomoneyna_be.score.vo.UserScoreVO;
import com.choogoomoneyna.choogoomoneyna_be.user.enums.ChoogooMi;
import com.choogoomoneyna.choogoomoneyna_be.user.service.UserService;
import com.choogoomoneyna.choogoomoneyna_be.user.vo.MatchedUserVO;
import com.choogoomoneyna.choogoomoneyna_be.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchingServiceImpl implements MatchingService {

    private final MatchingMapper matchingMapper;
    private final ScoreService scoreService;
    private final ScoreCalculateService scoreCalculateService;
    private final RoundInfoService roundInfoService;
    private final MatchingMissionResultService matchingMissionResultService;
    private final UserService userService;
    private final UserMatchingHistoryService userMatchingHistoryService;
    private final RankingService rankingService;
    private final RankingUpdateService rankingUpdateService;

    private int roundNumber;

    private void assignMatchMission(Long user1Id, Long user2Id, ChoogooMi choogooMi) {
        Long matchingId = matchingMapper.getProgressMatchingIdByUserId(user1Id);
        if (matchingId == null) {
            throw new IllegalArgumentException("Matching ID is null");
        }

        int commonMissionId = CommonMissionType.COMMON.getRandomId();
        List<Integer> choogooMiMissionIds = choogooMi.getMissionType().getRandomIds(2);

        matchingMissionResultService.createMatchingMissionResult(user1Id, matchingId, commonMissionId);
        if (!user1Id.equals(user2Id)) {
            matchingMissionResultService.createMatchingMissionResult(user2Id, matchingId, commonMissionId);
        }

        for (Integer choogooMiMissionId : choogooMiMissionIds) {
            matchingMissionResultService.createMatchingMissionResult(user1Id, matchingId, choogooMiMissionId);
            if (!user1Id.equals(user2Id)) {
                matchingMissionResultService.createMatchingMissionResult(user2Id, matchingId, choogooMiMissionId);
            }
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
                .filter(user -> choogooMi.name().equals(user.getChoogooMi()))
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
     * 데이터베이스의 라운드 정보를 업데이트하고 새로운 라운드를 생성합니다.
     * <br><br>
     * <b>처리 과정:</b>
     * <ol>
     *     <li>roundInfoService를 통해 최신 라운드 정보 조회</li>
     *     <li>다음 라운드 번호 계산 (현재 라운드 번호 + 1)</li>
     *     <li>다음 라운드의 시작일/종료일을 7일 뒤로 설정</li>
     *     <li>새로운 라운드 정보를 DB에 생성</li>
     * </ol>
     * <br>
     * <b>사용 컴포넌트:</b>
     * <ul>
     *     <li>roundInfoService: 라운드 정보 관리 서비스</li>
     *     <li>RoundInfoVO: 라운드 정보 값 객체</li>
     * </ul>
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
    @Transactional
    public void startAllMatching() {
        prepareNewRoundAndUpdateDates();

        List<UserVO> totalUsers = userService.findAllUsers();
        List<UserScoreVO> scores = scoreService.findCurrentAllScores(roundNumber-1);

        // ranking 과 score 테이블에도 이번 라운드에 대한 내용 생성
        List<UserScoreVO> newScores = new ArrayList<>();
        List<RankingVO> newRankings = new ArrayList<>();
        for (UserScoreVO score : scores) {
            newScores.add(UserScoreVO.builder()
                    .roundNumber(roundNumber)
                    .userId(score.getUserId())
                    .scoreValue(score.getScoreValue())
                    .isLevelUp(score.getIsLevelUp() != null && score.getIsLevelUp())
                    .build());

            newRankings.add(RankingVO.builder()
                    .roundNumber(roundNumber)
                    .userId(score.getUserId())
                    .build());

        }

        System.out.println("newScores: " + newScores.size());
        System.out.println("newRankings: " + newRankings.size());

        // score 테이블에 삽입
        scoreService.batchCreateScores(newScores);

        // ranking 테이블에 삽입 및 정렬
        rankingService.batchCreateRankings(newRankings);
        rankingUpdateService.updateRanking();

        Map<Long, Integer> scoreMap = scores.stream()
                .collect(Collectors.toMap(UserScoreVO::getUserId, UserScoreVO::getScoreValue));

        // User를 점수에 따라 내림차순 정렬
        for (ChoogooMi choogooMi : ChoogooMi.values()) {
            if (choogooMi.equals(ChoogooMi.O))
                continue;

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

        // 매칭 라운드
        int roundNumber = roundInfoService.getLatestRoundInfo().getRoundNumber();

        // 진행 중인 매칭 전체 가져오기
        List<MatchingVO> progressMatchings = matchingMapper.findAllProgressMatchings();

        for (MatchingVO progressMatching : progressMatchings) {
            long matchingId = progressMatching.getId();
            log.info("matchingId: {}",matchingId);

            long user1Id = progressMatching.getUser1Id();
            long user2Id = progressMatching.getUser2Id();

            int user1Score = matchingMissionResultService.getAllScoreByUserIdAndMatchingId(user1Id, matchingId);
            int user2Score = matchingMissionResultService.getAllScoreByUserIdAndMatchingId(user2Id, matchingId);
            log.info("user1Score: {}",user1Score);
            log.info("user2Score: {}",user2Score);

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

            scoreCalculateService.calculateScore(user1Id, roundNumber, user1Score);
            scoreCalculateService.calculateScore(user2Id, roundNumber, user2Score);
        }

        // ranking table 업데이트
        rankingUpdateService.updateRanking();

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
    public Long getProgressMatchingIdByUserIdAndRoundNumber(Long userId, Integer roundNumber) {
        return matchingMapper.getProgressMatchingIdByUserIdAndRoundNumber(userId, roundNumber);
    }

    @Override
    public Long getComponentUserIdByUserIdAndMatchingId(Long userId, Long matchingId) {
        return matchingMapper.getComponentUserIdByUserIdAndMatchingId(userId, matchingId);
    }

    @Override
    public List<Long> findAllUserIdInProgressMatching() {
        return matchingMapper.findAllUserIdInProgressMatching();
    }
}
