package com.choogoomoneyna.choogoomoneyna_be.mock.service;

import com.choogoomoneyna.choogoomoneyna_be.score.service.ScoreService;
import com.choogoomoneyna.choogoomoneyna_be.score.vo.UserScoreVO;
import com.choogoomoneyna.choogoomoneyna_be.user.dto.LoginType;
import com.choogoomoneyna.choogoomoneyna_be.user.mapper.UserMapper;
import com.choogoomoneyna.choogoomoneyna_be.user.vo.UserVO;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Slf4j
public class MockService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final ScoreService scoreService;
    private final Faker faker = new Faker( new Locale("ko"));

    public void createMockUser(int count) {
        log.info("service layer ok");
        for (int i = 2; i <= count; i++) {
            log.info("service layer for ok");
            UserVO user = new UserVO();
            String key = "mock" + i;
            user.setEmail(key+"@gmail.com");
            user.setPassword(passwordEncoder.encode(key));
            user.setNickname("mock"+i);
            user.setChoogooMi("A");
            user.setLoginType(LoginType.LOCAL.name());
            try {
                userMapper.insertUser(user);
                //점수테이블
                UserScoreVO userScoreVO = new UserScoreVO(user.getId(), user.getUserLevel());
                scoreService.createScore(userScoreVO);
                log.info("score table added{} ", i);
                //랭킹테이블


                log.info("✅ Created user {}", key);
            } catch (Exception e) {
                log.error("❌ 유저 생성 실패: {}", key, e);
            }
        }
    }


}
