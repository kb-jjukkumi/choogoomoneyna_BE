package com.choogoomoneyna.choogoomoneyna_be.match.service;

import com.choogoomoneyna.choogoomoneyna_be.score.service.ScoreService;
import com.choogoomoneyna.choogoomoneyna_be.score.vo.UserScoreVO;
import com.choogoomoneyna.choogoomoneyna_be.user.mapper.UserMapper;
import com.choogoomoneyna.choogoomoneyna_be.user.vo.MatchedUserVO;
import com.choogoomoneyna.choogoomoneyna_be.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

    private final UserMapper userMapper;
    private final ScoreService scoreService;

    @Override
    public List<List<MatchedUserVO>> getUserPairs() {
        List<UserVO> users = userMapper.findAllUsers();
        List<UserScoreVO> scores = scoreService.getAllScores();

        Map<Long, Integer> scoreMap = scores.stream()
                .collect(Collectors.toMap(UserScoreVO::getUserId, UserScoreVO::getScore));

        // User를 점수에 따라 내림차순 정렬
        List<MatchedUserVO> matchableUsers = users.stream()
                .map(user -> new MatchedUserVO(
                        user.getId(),
                        user.getEmail(),
                        user.getProfileImageUrl(),
                        scoreMap.getOrDefault(user.getId(), 0)))
                .sorted(Comparator.comparingInt(MatchedUserVO::getUserScore).reversed())
                .toList();

        return pairUsers(matchableUsers);
    }

    /**
     * 점수로 정렬된 유저들을 둘씩 매칭해주는 알고리즘 작성
     */
    private List<List<MatchedUserVO>> pairUsers(List<MatchedUserVO> users) {
        int userSize = users.size();
        boolean[] isMatched = new boolean[userSize];
        Random random = new Random();

        List<List<MatchedUserVO>> result = new ArrayList<>();

        // 10등 안으로 차이 나게 랜덤 매칭
        for (int i = 0; i < userSize; i++) {
            if (isMatched[i]) {
                continue;
            }

            List<MatchedUserVO> matchedUsers = new ArrayList<>();
            matchedUsers.add(users.get(i));
            isMatched[i] = true;

            for (int j = i + random.nextInt(1, 10); j < userSize; j++) {
                if (!isMatched[j]) {
                    matchedUsers.add(users.get(j));
                    isMatched[j] = true;

                    result.add(matchedUsers);
                    break;
                }
            }
        }

        // 매칭 안된 사람끼리 순서대로
        boolean one = false;
        List<MatchedUserVO> matchedUsers = new ArrayList<>();
        for (int i = 0; i < userSize; i++) {
            if (!isMatched[i]) {
                if (one) {
                    matchedUsers.add(users.get(i));
                    isMatched[i] = true;
                    result.add(matchedUsers);
                    one = false;
                } else {
                    matchedUsers = new ArrayList<>();
                    matchedUsers.add(users.get(i));
                    isMatched[i] = true;
                    one = true;
                }
            }
        }

        // 한명이 남음
        // TODO: dummy data로 넣도록 수정
        if (one) {
            matchedUsers.add(users.get(userSize - 1));
            result.add(matchedUsers);
        }

        return result;
    }
}
