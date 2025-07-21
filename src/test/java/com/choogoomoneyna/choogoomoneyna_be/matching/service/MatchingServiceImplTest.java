package com.choogoomoneyna.choogoomoneyna_be.matching.service;

import com.choogoomoneyna.choogoomoneyna_be.score.service.ScoreService;
import com.choogoomoneyna.choogoomoneyna_be.score.vo.UserScoreVO;
import com.choogoomoneyna.choogoomoneyna_be.user.mapper.UserMapper;
import com.choogoomoneyna.choogoomoneyna_be.user.vo.MatchedUserVO;
import com.choogoomoneyna.choogoomoneyna_be.user.vo.UserVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MatchingServiceImplTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private ScoreService scoreService;

    @InjectMocks
    private MatchingServiceImpl matchService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetUserPairsWithManyUsers() {
        // given
        int totalUsers = 16215;

        List<UserVO> userList = new ArrayList<>();
        List<UserScoreVO> scoreList = new ArrayList<>();

        for (long i = 0; i < totalUsers; i++) {
            userList.add(createUser(i, "user" + i + "@test.com", "profile" + i + ".jpg"));
            int score = 60 + (int)(Math.random() * 41); // 60~100 사이 점수
            scoreList.add(new UserScoreVO(i, score));
        }

        when(userMapper.findAllUsers()).thenReturn(userList);
        when(scoreService.getAllScores()).thenReturn(scoreList);

        // when
        List<List<MatchedUserVO>> pairs = matchService.getUserPairs();

        // then
        assertNotNull(pairs);

        // 총 유저 수와 매칭된 유저 수 비교
        System.out.println(pairs.size() + " " + (totalUsers + 1) / 2);
        assertEquals(pairs.size(), (totalUsers + 1) / 2);

        // 각 그룹은 2명
        for (List<MatchedUserVO> pair : pairs) {
            assertTrue(pair.size() == 2);
        }

        verify(userMapper, times(1)).findAllUsers();
        verify(scoreService, times(1)).getAllScores();
    }

    private UserVO createUser(Long id, String email, String profileImage) {
        return UserVO.builder()
                .id(id)
                .email(email)
                .nickname("user" + id)
                .profileImageUrl(profileImage)
                .build();
    }
}
