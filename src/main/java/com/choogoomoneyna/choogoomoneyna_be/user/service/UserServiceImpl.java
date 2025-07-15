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
    public boolean findByEmailAndLoginType(String email, LoginType loginType) {
        return userMapper.existsByEmail(email, loginType.name());
    }

    @Override
    public void updateChoogooMiByEmail(String email, ChoogooMi choogooMi) {
        // db에서 email 을 이용하여 user 찾기
        UserVO user = userMapper.findByEmail(email);
        
        // 추구미 수정
        user.setChoogooMi(choogooMi.name());
        
        // db에 다시 업데이트
        userMapper.updateUser(user);
        log.info("User's choogooMi updated: {}", user);
    }
}
