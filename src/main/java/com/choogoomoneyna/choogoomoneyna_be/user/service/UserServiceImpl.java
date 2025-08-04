package com.choogoomoneyna.choogoomoneyna_be.user.service;

import com.choogoomoneyna.choogoomoneyna_be.user.enums.ChoogooMi;
import com.choogoomoneyna.choogoomoneyna_be.user.enums.LoginType;
import com.choogoomoneyna.choogoomoneyna_be.user.mapper.UserMapper;
import com.choogoomoneyna.choogoomoneyna_be.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public void insertUser(UserVO user) {
        userMapper.insertUser(user);
    }

    @Override
    public boolean isUserLoginIdDuplicated(String nickname) {
        return userMapper.existsByNickname(nickname.trim());
    }

    @Override
    public boolean existsByEmailAndLoginType(String email, LoginType loginType) {
        return userMapper.existsByEmail(email, loginType.name());
    }

    @Override
    public UserVO findByEmailAndLoginType(String email, LoginType loginType) {
        return userMapper.findByEmailAndLoginType(email, loginType.name());
    }

    @Override
    public void updateChoogooMiByUserId(Long userId, ChoogooMi choogooMi) {
        // db에서 userId를 이용하여 user 찾기
        UserVO user = userMapper.findById(userId);
        
        // 추구미 수정
        user.setChoogooMi(choogooMi.name());
        
        // db에 다시 업데이트
        userMapper.updateUser(user);
        log.info("User's choogooMi updated: {}", user);
    }

    @Override
    public void updateUserNicknameByUserId(Long userId, String nickname) {
        // db 에서 userId를 이용하여 user 찾기
        UserVO user = userMapper.findById(userId);

        // nickname 수정
        user.setNickname(nickname);

        // db에 다시 업데이트
        userMapper.updateUser(user);
        log.info("User's nickname updated: {}", user);
    }

    @Override
    public void updateProfileImageUrlByUserId(Long userId, String profileImageUrl) {
        // db 에서 userId를 이용하여 user 찾기
        UserVO user = userMapper.findById(userId);

        // profileImageUrl 수정
        user.setProfileImageUrl(profileImageUrl);

        // db에 다시 업데이트
        userMapper.updateUser(user);
        log.info("User's profileImageUrl updated: {}", user);
    }

    @Override
    public void updatePasswordByUserEmail(String email, String password) {
        userMapper.updatePasswordByUserEmail(email, password);
    }

    @Override
    public int countAllUsers() {
        return userMapper.countAllUsers();
    }

    @Override
    public ChoogooMi getChoogooMiByUserId(Long userId) {
        return ChoogooMi.valueOf(userMapper.getChoogooMiByUserId(userId));
    }

    @Override
    public String getNicknameByUserId(Long userId) {
        return userMapper.getNicknameByUserId(userId);
    }

    @Override
    public List<UserVO> findAllUsers() {
        return userMapper.findAllUsers();
    }
}
