package com.choogoomoneyna.choogoomoneyna_be.user.service;

import com.choogoomoneyna.choogoomoneyna_be.exception.CustomException;
import com.choogoomoneyna.choogoomoneyna_be.exception.ErrorCode;
import com.choogoomoneyna.choogoomoneyna_be.user.enums.LoginType;
import com.choogoomoneyna.choogoomoneyna_be.user.vo.UserVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserPasswordServiceImplTest {

    @Mock private UserService userService;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserPasswordServiceImpl userPasswordService;

    @Test
    void resetPassword_success() {
        // given
        String email = "test@example.com";
        String newPassword = "<PASSWORD>";
        String newPasswordConfirm = "<PASSWORD>";

        UserVO user = new UserVO();
        user.setEmail(email);

        given(userService.findByEmailAndLoginType(email, LoginType.LOCAL)).willReturn(user);
        given(passwordEncoder.encode(newPassword)).willReturn("encodedPassword");

        // when
        userPasswordService.resetPassword(email, newPassword, newPasswordConfirm);

        // then
        ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> passwordCaptor = ArgumentCaptor.forClass(String.class);
        then(userService).should().updatePasswordByUserEmail(emailCaptor.capture(), passwordCaptor.capture());

        assertEquals(email, emailCaptor.getValue());
        assertEquals("encodedPassword", passwordCaptor.getValue());
    }

    @Test
    void resetPassword_passwordsNotMatching_throwsException() {
        // given
        String email = "test@example.com";
        String newPassword = "<PASSWORD>";
        String newPasswordConfirm = "<NOT PASSWORD>";

        // when
        CustomException exception = assertThrows(CustomException.class,
                () -> userPasswordService.resetPassword(email, newPassword, newPasswordConfirm));

        // then
        assertEquals(ErrorCode.NOT_EQUAL_PASSWORD_CONFIRM, exception.getErrorCode());
    }

    @Test
    void resetPassword_emailNotFound_throwsException() {
        // given
        String email = "test@example.com";
        String newPassword = "<PASSWORD>";
        String newPasswordConfirm = "<PASSWORD>";
        given(userService.findByEmailAndLoginType(email, LoginType.LOCAL)).willReturn(null);

        // when
        CustomException exception = assertThrows(CustomException.class,
                () -> userPasswordService.resetPassword(email, newPassword, newPasswordConfirm));

        // then
        assertEquals(ErrorCode.EMAIL_NOT_FOUND, exception.getErrorCode());
    }
}