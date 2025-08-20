package com.choogoomoneyna.choogoomoneyna_be.user.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class PasswordValidatorTest {

    @Test
    @DisplayName("비밀번호 형식이 유효한 경우")
    void isValidFormat_validPassword() {
        // given
        String password = "<PASSWORD>";

        // when
        boolean result = PasswordValidator.isValidFormat(password);

        // then
        assertTrue(result);
    }

    @Test
    @DisplayName("비밃번호 형식이 잘못된 경우 - too short")
    void isValidFormat_tooShort() {
        // given
        String password = "<>";

        // when
        boolean result = PasswordValidator.isValidFormat(password);

        // then
        assertFalse(result);
    }

    @Test
    @DisplayName("비밀번호가 일치하는 경우")
    void isMatching_samePassword() {
        // given
        String password = "<PASSWORD>";
        String passwordConfirm = "<PASSWORD>";

        // when
        boolean result = PasswordValidator.isMatching(password, passwordConfirm);

        // then
        assertTrue(result);
    }

    @Test
    @DisplayName("비밀번호가 불일치하는 경우")
    void isMatching_diffPassword() {
        // given
        String password = "<PASSWORD>!";
        String passwordConfirm = "<NOT PASSWORD>!";

        // when
        boolean result = PasswordValidator.isMatching(password, passwordConfirm);

        // then
        assertFalse(result);
    }
}