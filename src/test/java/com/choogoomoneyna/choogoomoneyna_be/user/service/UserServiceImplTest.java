package com.choogoomoneyna.choogoomoneyna_be.user.service;

import com.choogoomoneyna.choogoomoneyna_be.auth.jwt.service.AuthServiceImpl;
import com.choogoomoneyna.choogoomoneyna_be.user.enums.ChoogooMi;
import com.choogoomoneyna.choogoomoneyna_be.user.enums.LoginType;
import com.choogoomoneyna.choogoomoneyna_be.user.dto.request.UserJoinRequestDTO;
import com.choogoomoneyna.choogoomoneyna_be.user.mapper.UserMapper;
import com.choogoomoneyna.choogoomoneyna_be.user.vo.UserVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser() {
        // given
        UserJoinRequestDTO dto = UserJoinRequestDTO.builder()
                .email("leehk@example.com")
                .password("<PASSWORD>")
                .nickname("leehk")
                .profileImage(null)
                .choogooMi(ChoogooMi.A)
                .build();

        String encryptedPassword = "ENCODED_PASSWORD";
        when(passwordEncoder.encode(dto.getPassword())).thenReturn(encryptedPassword);

        // when
        authService.registerUser(dto);

        // then
        // ArgumentCaptor 객체를 생성해서 UserVO 타입의 인자를 캡처할 준비
        ArgumentCaptor<UserVO> captor = ArgumentCaptor.forClass(UserVO.class);

        // userMapper.insertUser() 메서드가 호출되었는지 검증하고,
        // 그때 전달된 인자를 captor로 캡처함
        verify(userMapper).insertUser(captor.capture());

        // 캡처된 UserVO 객체를 꺼내와 savedUser 변수에 저장
        UserVO savedUser = captor.getValue();
        
        // 작성이 잘 되는지 확인
        assertThat(savedUser.getPassword()).isEqualTo(encryptedPassword);
        assertThat(savedUser.getNickname()).isEqualTo("leehk");
        assertThat(savedUser.getChoogooMi()).isEqualTo(ChoogooMi.A.name());
    }

    @Test
    void isUserLoginIdDuplicated() {
        // given
        when(userMapper.existsByNickname("leehk")).thenReturn(true);

        // when
        boolean result = userService.isUserLoginIdDuplicated("leehk");

        // then
        assertThat(result).isTrue();
    }

    @Test
    void existsByEmailAndLoginType() {
        // given
        String email = "leehk@example.com";
        LoginType loginType = LoginType.LOCAL;

        when(userMapper.existsByEmail(email, loginType.name())).thenReturn(true);

        // when
        boolean result = userService.existsByEmailAndLoginType(email, loginType);

        // then
        assertThat(result).isTrue();
        verify(userMapper).existsByEmail(email, loginType.name());
    }

    @Test
    void updateChoogooMiByUserId() {
        // given
        UserVO user = new UserVO();
        user.setId(1L);
        user.setChoogooMi(ChoogooMi.A.name());

        when(userMapper.findById(1L)).thenReturn(user);

        // when
        userService.updateChoogooMiByUserId(1L, ChoogooMi.B);

        // then
        assertThat(user.getChoogooMi()).isEqualTo(ChoogooMi.B.name());
        verify(userMapper).updateUser(user);
    }

    @Test
    void updateUserNicknameByUserId() {
        // given
        UserVO user = new UserVO();
        user.setId(1L);
        user.setNickname("dlgkrwns213");

        when(userMapper.findById(1L)).thenReturn(user);

        // when
        userService.updateUserNicknameByUserId(1L, "leehk");

        // then
        assertThat(user.getNickname()).isEqualTo("leehk");
        verify(userMapper).updateUser(user);
    }

    @Test
    void countAllUsers() {
        when(userMapper.countAllUsers()).thenReturn(10);
        int result = userService.countAllUsers();
        assertThat(result).isEqualTo(10);
    }
}