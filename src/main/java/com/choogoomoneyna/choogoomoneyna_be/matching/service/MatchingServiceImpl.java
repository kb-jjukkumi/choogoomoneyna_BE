package com.choogoomoneyna.choogoomoneyna_be.matching.service;

import com.choogoomoneyna.choogoomoneyna_be.matching.dto.MatchingStatus;
import com.choogoomoneyna.choogoomoneyna_be.matching.enums.ChoogooMiMissionType;
import com.choogoomoneyna.choogoomoneyna_be.matching.enums.CommonMissionType;
import com.choogoomoneyna.choogoomoneyna_be.matching.mapper.MatchingMapper;
import com.choogoomoneyna.choogoomoneyna_be.matching.vo.MatchingVO;
import com.choogoomoneyna.choogoomoneyna_be.score.service.ScoreService;
import com.choogoomoneyna.choogoomoneyna_be.score.vo.UserScoreVO;
import com.choogoomoneyna.choogoomoneyna_be.user.dto.ChoogooMi;
import com.choogoomoneyna.choogoomoneyna_be.user.mapper.UserMapper;
import com.choogoomoneyna.choogoomoneyna_be.user.service.UserService;
import com.choogoomoneyna.choogoomoneyna_be.user.vo.MatchedUserVO;
import com.choogoomoneyna.choogoomoneyna_be.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchingServiceImpl implements MatchingService {

    private final UserMapper userMapper;
    private final MatchingMapper matchingMapper;
    private final ScoreService scoreService;
    private final RoundInfoService roundInfoService;
    private final MatchingMissionResultService matchingMissionResultService;
    private final UserService userService;

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
        // 시스템 시간대를 기준으로 Date 객체로 변환
        Date mondayDate = roundInfoService.getStartDate();
        Date sundayDate = roundInfoService.getEndDate();

        return MatchingVO.builder()
                .user1Id(user1Id)
                .user2Id(user2Id)
                .matchingStatus(MatchingStatus.PENDING.name())
                .matchingStart(mondayDate)
                .matchingFinish(sundayDate)
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

    @Override
    public void startAllMatching() {
        // TODO: UserService 로 수정 할 것!
        List<UserVO> totalUsers = userMapper.findAllUsers();
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
        assignMatchMission(userId, userId, userService.getChoogooMiByUserId(userId););
    }

    @Override
    @Transactional
    public void finishAllMatching() {
        // 진행 중인 매칭 전체 가져오기
        List<MatchingVO> progressMatchings = matchingMapper.findAllProgressMatchings();
        
        // TODO: 결과를 users table에 저장할 것!

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
}
