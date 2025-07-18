package com.choogoomoneyna.choogoomoneyna_be.auth.email.service;

import com.choogoomoneyna.choogoomoneyna_be.auth.email.util.AuthCodeGenerator;
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
    private final Map<String, String> codeStorage = new ConcurrentHashMap<>();

    @Override
    public void sendAuthCode(String email) {
        String code = AuthCodeGenerator.generate();
        codeStorage.put(email, code);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("이메일 인증 코드");
        message.setText("인증 코드는 다음과 같습니다" + code);

        mailSender.send(message);
    }

    @Override
    public boolean verifyAuthCode(String email, String code) {
        String storedCode = codeStorage.get(email);
        if (storedCode == null) {
            return false;
        }
        return storedCode.equals(code);
    }
}
