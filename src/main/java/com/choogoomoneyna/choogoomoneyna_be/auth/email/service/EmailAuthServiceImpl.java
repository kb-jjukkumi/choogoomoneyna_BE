package com.choogoomoneyna.choogoomoneyna_be.auth.email.service;

import com.choogoomoneyna.choogoomoneyna_be.auth.email.util.AuthCodeGenerator;
import com.choogoomoneyna.choogoomoneyna_be.auth.email.vo.AuthCodeData;
import lombok.RequiredArgsConstructor;
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
        String code = AuthCodeGenerator.generate();
        long expireAt = System.currentTimeMillis() + (5 * 60 * 1000);
        codeStorage.put(email, new AuthCodeData(code, expireAt));

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("이메일 인증 코드");
        message.setText("인증 코드는 다음과 같습니다.\n\n" + code);

        mailSender.send(message);
    }

    @Override
    public boolean verifyAuthCode(String email, String code) {
        AuthCodeData storedCode = codeStorage.get(email);
        if (storedCode == null || storedCode.isExpired()) {
            return false;
        }
        boolean isValid = storedCode.code().equals(code);
        if (isValid) {
            codeStorage.remove(email); // 재사용 방지
        }
        return isValid;
    }
}
