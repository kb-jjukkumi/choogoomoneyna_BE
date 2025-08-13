package com.choogoomoneyna.choogoomoneyna_be.auth.email.service;

import com.choogoomoneyna.choogoomoneyna_be.auth.email.util.AuthCodeGenerator;
import com.choogoomoneyna.choogoomoneyna_be.auth.email.vo.AuthCodeData;
import com.choogoomoneyna.choogoomoneyna_be.exception.CustomException;
import com.choogoomoneyna.choogoomoneyna_be.exception.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class EmailAuthServiceImpl implements EmailAuthService {

    private final JavaMailSender mailSender;
    private final Map<String, AuthCodeData> codeStorage = new ConcurrentHashMap<>();

    @Override
    public void sendAuthCode(String email) {
        try {
            String code = AuthCodeGenerator.generate();
            long expireAt = System.currentTimeMillis() + (5 * 60 * 1000);
            codeStorage.put(email, new AuthCodeData(code, expireAt));

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("이메일 인증 코드");
            message.setText("인증 코드는 다음과 같습니다.\n\n" + code + "\n\n5분 내로 입력해주세요.");

            mailSender.send(message);
        } catch (MailException e) {
            throw new CustomException(ResponseCode.EMAIL_SEND_FAILURE);
        } catch (Exception e) {
            throw new CustomException(
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "이메일 인증 코드 생성/전송 중 알 수 없는 오류가 발생했습니다.",
                    e
            );
        }
    }

    @Override
    public boolean verifyAuthCode(String email, String code) {
        AuthCodeData storedCode = codeStorage.get(email);

        if (storedCode == null) {
            throw new CustomException(
                    ResponseCode.AUTH_VALIDATION_ERROR,
                    "해당 이메일로 발급된 인증 코드가 없습니다."
            );
        }

        if (storedCode.isExpired()) {
            codeStorage.remove(email);
            throw new CustomException(
                    ResponseCode.AUTH_VALIDATION_ERROR,
                    "인증 코드가 만료되었습니다."
            );
        }

        if (!storedCode.code().equals(code)) {
            throw new CustomException(
                    ResponseCode.AUTH_VALIDATION_ERROR,
                    "인증 코드가 올바르지 않습니다."
            );
        }

        codeStorage.remove(email);
        return true;
    }
}
