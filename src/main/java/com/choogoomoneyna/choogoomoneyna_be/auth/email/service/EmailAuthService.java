package com.choogoomoneyna.choogoomoneyna_be.auth.email.service;

import com.choogoomoneyna.choogoomoneyna_be.exception.CustomException;

public interface EmailAuthService {

    /**
     * 지정된 이메일 주소로 인증 코드를 전송합니다.
     * 인증 코드는 생성 후 임시 저장되며,
     * 코드가 포함된 이메일이 수신자에게 발송됩니다.
     *
     * @param email 인증 코드가 전송될 이메일 주소
     * @throws CustomException 이메일 전송 실패 또는
     *                         처리 중 내부 오류 발생 시
     */
    void sendAuthCode(String email);

    /**
     * 지정된 이메일에 대한 인증 코드의 유효성을 검증합니다.
     * 코드가 존재하고, 만료되지 않았으며, 저장된 값과 일치하는 경우 유효한 것으로 간주됩니다.
     * 검증이 성공하면 저장소에서 코드가 제거됩니다.
     *
     * @param email 인증 코드가 발급된 이메일 주소
     * @param code 검증할 인증 코드
     * @return 인증 코드가 유효한 경우 true, 그렇지 않으면 false
     * @throws CustomException 이메일에 대한 코드가 존재하지 않거나, 코드가 만료되었거나, 코드가 유효하지 않은 경우
     */
    boolean verifyAuthCode(String email, String code);
}
