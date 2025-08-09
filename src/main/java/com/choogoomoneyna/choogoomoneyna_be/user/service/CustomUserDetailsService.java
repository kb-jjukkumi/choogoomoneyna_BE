package com.choogoomoneyna.choogoomoneyna_be.user.service;

import com.choogoomoneyna.choogoomoneyna_be.auth.jwt.util.CustomUserDetails;
import com.choogoomoneyna.choogoomoneyna_be.user.mapper.UserMapper;
import com.choogoomoneyna.choogoomoneyna_be.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Spring Security의 인증 시스템을 사용하기 위한 클래스
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserMapper userMapper;

    /**
     * user ID를 기반으로 사용자 상세 정보를 로드
     * userId 를 이용하여 사용자를 찾고
     * UserDetails class로 변환시킴
     *
     * @param userId 조회할 사용자의 ID
     * @return UserDetails 사용자 인증 정보
     * @throws UsernameNotFoundException 사용자를 찾을 수 없을 경우 발생
     */
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        // DB 에서 사용자 정보 조회
        UserVO user = userMapper.findById(Long.parseLong(userId));
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return new CustomUserDetails(user);
    }
}
