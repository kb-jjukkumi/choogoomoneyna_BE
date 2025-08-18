package com.choogoomoneyna.choogoomoneyna_be.report.service;

import com.choogoomoneyna.choogoomoneyna_be.config.GptConfig;
import com.choogoomoneyna.choogoomoneyna_be.report.dto.request.GptRequestDTO;
import com.choogoomoneyna.choogoomoneyna_be.report.dto.response.GptResponseDTO;
import com.choogoomoneyna.choogoomoneyna_be.report.dto.response.OpenAiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpHeaders;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AiClient {

    private final RestTemplate restTemplate;
    private final GptConfig gptConfig;

    @Value("${openai.api.url}")
    private String OPENAI_URL;

    /**
     * OpenAI API에 프롬프트를 전송하고 응답을 처리하여 GptResponseDTO 객체를 생성합니다.
     *
     * 이 메서드는 적절한 헤더와 페이로드로 요청을 구성하여 OpenAI API에 전송하고, 
     * 응답에서 필요한 정보를 추출하여 처리합니다. 응답의 JSON 콘텐츠를 파싱하여 
     * GptResponseDTO 객체로 매핑합니다.
     *
     * @param prompt OpenAI API에서 처리할 사용자의 쿼리 또는 정보를 나타내는 입력 문자열
     * @return 처리된 결과를 포함하는 GptResponseDTO 객체 (요약, 조언, 추천사항, 
     *         실행항목, 총 지출 및 카테고리별 지출 통계 포함)
     * @throws RuntimeException 응답 실패, 응답 본문이 비어있음, JSON 콘텐츠 파싱 실패 
     *                         또는 choices가 비어있는 경우 발생
     */
    public GptResponseDTO requestGpt(String prompt) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(gptConfig.getApiKey());
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        GptRequestDTO requestDTO = GptRequestDTO.builder()
                .model(gptConfig.getModel())
                .messages(List.of(new GptRequestDTO.Message("user", prompt)))
                .build();

        HttpEntity<GptRequestDTO> request = new HttpEntity<>(requestDTO, httpHeaders);
        ResponseEntity<OpenAiResponse> response = restTemplate.postForEntity(
                OPENAI_URL, request, OpenAiResponse.class
        );

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            OpenAiResponse body = response.getBody();

            if (body.choices != null && !body.choices.isEmpty()) {
                String content = body.choices.get(0).message.content;
                System.out.println(content);

                // content 안에 JSON 문자열 파싱
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    return objectMapper.readValue(content, GptResponseDTO.class);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException("GPT content JSON 파싱 실패", e);
                }
            } else {
                throw new RuntimeException("GPT choices 비어있음");
            }
        } else {
            throw new RuntimeException("OpenAI 호출 실패: " + response.getStatusCode());
        }
    }

}
