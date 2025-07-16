package com.choogoomoneyna.choogoomoneyna_be.user.service;

import com.choogoomoneyna.choogoomoneyna_be.user.dto.ChoogooMi;
import com.choogoomoneyna.choogoomoneyna_be.user.dto.LoginType;
import com.choogoomoneyna.choogoomoneyna_be.user.dto.request.UserJoinRequestDTO;
import com.choogoomoneyna.choogoomoneyna_be.user.mapper.UserMapper;
import com.choogoomoneyna.choogoomoneyna_be.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void registerUser(UserJoinRequestDTO dto) {
        String encryptedPassword = passwordEncoder.encode(dto.getPassword());

        UserVO userVO = UserConverter.joinRequestDtoToVo(dto, LoginType.LOCAL, encryptedPassword);

        userMapper.insertUser(userVO);
        log.info("User registered: {}", userVO);
    }

    @Override
    public boolean isUserLoginIdDuplicated(String nickname) {
        return userMapper.existsByNickname(nickname);
    }

    @Override
    public boolean existsByEmailAndLoginType(String email, LoginType loginType) {
        return userMapper.existsByEmail(email, loginType.name());
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
    public int countAllUsers() {
        return userMapper.countAllUsers();
    }
}
