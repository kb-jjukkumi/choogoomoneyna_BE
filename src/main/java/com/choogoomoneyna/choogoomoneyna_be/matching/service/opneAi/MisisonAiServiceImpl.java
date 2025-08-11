package com.choogoomoneyna.choogoomoneyna_be.matching.service.opneAi;

import com.choogoomoneyna.choogoomoneyna_be.matching.dto.Response.TextAiResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MisisonAiServiceImpl implements MissionAiService {

    private final OpenAiClient openAiClient;

    @Override
    public TextAiResponseDTO validateMission3(String mission, String answer) {

        String prompt = generatePrompt(mission, answer);

        TextAiResponseDTO response = openAiClient.requestGpt(prompt);
        if (response == null) {
            throw new RuntimeException("GPT 오류");
        }
        return response;
    }

    private String generatePrompt(String mission, String answer) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("""
                다음은 자산관리 서비스 중 사용자가 텍스트를 입력하는 미션의 질문 내용과 사용자 응답입니다.
                질문 내용을 파악해서 사용자가 입력한 내용이 질문의 의도에 일치하는 지 100점 만점으로 점수를 내 주세요.
                
                예시1) 질문 : 내 자산관리 목표 한 마디 작성
                사용자 응답 : ㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇ
                이면
                점수 : 0
                
                예시2) 질문 : 내 자산관리 목표 한 마디 작성
                사용자 응답 : 나는 자산관리 목표로 저축형을 선택했다. 최대한 저축 비율을 늘려서 목돈을 만들거다. 지출을 최소화해서 저축할 돈을 모으자.
                점수 : 90
                
                - 응답을 JSON 형식으로 해줘. 코드블럭 없이, 마크다운 없이 순수 JSON만 보내줘.
                
                JSON 구조 :
                {
                    "score" : 점수
                }
                
                예시 :
                {
                    "score" : 90
                }
                """);

        prompt.append("질문 : ").append(mission);
        prompt.append("\n");
        prompt.append("사용자 응답: ").append(answer);

        log.info(prompt.toString());
        return prompt.toString();
    }
}
