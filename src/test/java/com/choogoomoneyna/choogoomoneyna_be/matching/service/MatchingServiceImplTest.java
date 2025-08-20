package com.choogoomoneyna.choogoomoneyna_be.matching.service;

import com.choogoomoneyna.choogoomoneyna_be.exception.CustomException;
import com.choogoomoneyna.choogoomoneyna_be.matching.mapper.MatchingMapper;
import com.choogoomoneyna.choogoomoneyna_be.matching.vo.MatchingVO;
import com.choogoomoneyna.choogoomoneyna_be.matching.vo.RoundInfoVO;
import com.choogoomoneyna.choogoomoneyna_be.ranking.service.RankingService;
import com.choogoomoneyna.choogoomoneyna_be.ranking.service.RankingUpdateService;
import com.choogoomoneyna.choogoomoneyna_be.score.service.ScoreCalculateService;
import com.choogoomoneyna.choogoomoneyna_be.score.service.ScoreService;
import com.choogoomoneyna.choogoomoneyna_be.score.vo.UserScoreVO;
import com.choogoomoneyna.choogoomoneyna_be.user.enums.ChoogooMi;
import com.choogoomoneyna.choogoomoneyna_be.user.mapper.UserMapper;
import com.choogoomoneyna.choogoomoneyna_be.user.service.UserService;
import com.choogoomoneyna.choogoomoneyna_be.user.vo.MatchedUserVO;
import com.choogoomoneyna.choogoomoneyna_be.user.vo.UserVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MatchingServiceImplTest {

    @Mock private MatchingMapper matchingMapper;
    @Mock private ScoreService scoreService;
    @Mock private ScoreCalculateService scoreCalculateService;
    @Mock private RoundInfoService roundInfoService;
    @Mock private MatchingMissionResultService matchingMissionResultService;
    @Mock private UserService userService;
    @Mock private UserMatchingHistoryService userMatchingHistoryService;
    @Mock private RankingService rankingService;
    @Mock private RankingUpdateService rankingUpdateService;

    @InjectMocks
    private MatchingServiceImpl matchingService;

    @BeforeEach
    void setup() {
        // roundInfoService.getLatestRoundInfo() 반환값 설정
        RoundInfoVO roundInfoVO = RoundInfoVO.builder()
                .roundNumber(1)
                .startDate(new Date())
                .endDate(new Date())
                .build();
        when(roundInfoService.getLatestRoundInfo()).thenReturn(roundInfoVO);
    }

    @Test
    void startMatchingTest() {
        // given
        UserVO user1 = createUser(1L, "<EMAIL>", ChoogooMi.A, "http://example.com/profile1.png");
        UserVO user2 = createUser(2L, "<EMAIL>", ChoogooMi.A, "http://example.com/profile2.png");
        UserVO user3 = createUser(3L, "<EMAIL>", ChoogooMi.A, "http://example.com/profile3.png");
        UserVO user4 = createUser(4L, "<EMAIL>", ChoogooMi.A, "http://example.com/profile4.png");
        UserVO user5 = createUser(5L, "<EMAIL>", ChoogooMi.A, "http://example.com/profile5.png");
        UserVO user6 = createUser(6L, "<EMAIL>", ChoogooMi.A, "http://example.com/profile6.png");
        UserVO user7 = createUser(7L, "<EMAIL>", ChoogooMi.B, "http://example.com/profile7.png");
        UserVO user8 = createUser(8L, "<EMAIL>", ChoogooMi.B, "http://example.com/profile8.png");
        UserVO user9 = createUser(9L, "<EMAIL>", ChoogooMi.B, "http://example.com/profile9.png");
        UserVO user10 = createUser(10L, "<EMAIL>", ChoogooMi.C, "http://example.com/profile10.png");

        List<UserVO> users = Arrays.asList(user1, user2, user3, user4, user5, user6, user7, user8, user9, user10);
        when(userService.findAllUsers()).thenReturn(users);

        UserScoreVO score1 = createUserScore(1L, 100);
        UserScoreVO score2 = createUserScore(2L, 98);
        UserScoreVO score3 = createUserScore(3L, 90);
        UserScoreVO score4 = createUserScore(4L, 97);
        UserScoreVO score5 = createUserScore(5L, 85);
        UserScoreVO score6 = createUserScore(6L, 3);
        UserScoreVO score7 = createUserScore(7L, 100);
        UserScoreVO score8 = createUserScore(8L, 80);
        UserScoreVO score9 = createUserScore(9L, 2);
        UserScoreVO score10 = createUserScore(10L, 111);
        List<UserScoreVO> scores = Arrays.asList(score1, score2, score3, score4, score5, score6, score7, score8, score9, score10);
        when(scoreService.findCurrentAllScores(anyInt())).thenReturn(scores);

        // when
        assertDoesNotThrow(() -> matchingService.startAllMatching());

        // then
        verify(scoreService, times(1)).batchCreateScores(anyList());
        verify(rankingService, times(1)).batchCreateRankings(anyList());
        verify(rankingUpdateService, times(1)).updateRanking();
        verify(matchingMapper, atLeastOnce()).insertMatching(any(MatchingVO.class));

        // 6개 조(A:3, B:2, C:1)로 나뉘어야함
        verify(matchingMapper, times(6)).insertMatching(any(MatchingVO.class));

    }

    @Test
    void startAllMatching_emptyUsers_throwsException() {
        // given
        when(userService.findAllUsers()).thenReturn(Collections.emptyList());

        // when & then
        assertThrows(CustomException.class, () -> matchingService.startAllMatching());
    }

    @Test
    void startAllMatching_emptyScores_throwsException() {
        // given
        UserVO user = UserVO.builder().id(1L).choogooMi(ChoogooMi.A.name()).build();
        when(userService.findAllUsers()).thenReturn(List.of(user));
        when(scoreService.findCurrentAllScores(anyInt())).thenReturn(Collections.emptyList());

        // when & then
        assertThrows(CustomException.class, () -> matchingService.startAllMatching());
    }

    private UserVO createUser(Long id, String email, ChoogooMi choogooMi, String profileImage) {
        return UserVO.builder()
                .id(id)
                .email(email)
                .choogooMi(choogooMi.name())
                .nickname("user" + id)
                .profileImageUrl(profileImage)
                .build();
    }

    private UserScoreVO createUserScore(Long id, int score) {
        return UserScoreVO.builder()
                .roundNumber(1)
                .userId(id)
                .scoreValue(score)
                .build();
    }
}
