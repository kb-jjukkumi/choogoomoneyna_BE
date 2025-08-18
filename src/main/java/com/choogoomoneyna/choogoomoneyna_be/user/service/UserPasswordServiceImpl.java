package com.choogoomoneyna.choogoomoneyna_be.user.service;

import com.choogoomoneyna.choogoomoneyna_be.exception.CustomException;
import com.choogoomoneyna.choogoomoneyna_be.exception.ErrorCode;
import com.choogoomoneyna.choogoomoneyna_be.user.dto.request.UserPasswordResetDTO;
import com.choogoomoneyna.choogoomoneyna_be.user.enums.LoginType;
import com.choogoomoneyna.choogoomoneyna_be.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPasswordServiceImpl implements UserPasswordService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void resetPassword(String email, String newPassword, String newPasswordConfirm) {
        // 비밀번호 형식 체크
        if (!PasswordValidator.isValidFormat(newPassword)) {
            throw new CustomException(ErrorCode.BAD_PASSWORD_FORMAT);
        }

        // 비밀번호 일치 여부 체크
        if (!PasswordValidator.isMatching(newPassword, newPasswordConfirm)) {
            throw new CustomException(ErrorCode.NOT_EQUAL_PASSWORD_CONFIRM);
        }

        UserVO user = userService.findByEmailAndLoginType(email, LoginType.LOCAL);
        if (user == null) {
            throw new CustomException(ErrorCode.EMAIL_NOT_FOUND);
        }

        String encryptedPassword = passwordEncoder.encode(newPassword);
        userService.updatePasswordByUserEmail(email, encryptedPassword);
    }
}
