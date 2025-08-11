package com.choogoomoneyna.choogoomoneyna_be.matching.service.opneAi;

import com.choogoomoneyna.choogoomoneyna_be.config.GptConfig;
import com.choogoomoneyna.choogoomoneyna_be.matching.dto.Response.TextAiResponseDTO;
import com.choogoomoneyna.choogoomoneyna_be.report.dto.request.GptRequestDTO;
import com.choogoomoneyna.choogoomoneyna_be.report.dto.response.GptResponseDTO;
import com.choogoomoneyna.choogoomoneyna_be.report.dto.response.OpenAiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OpenAiClient {

    private final RestTemplate restTemplate;
    private final GptConfig gptConfig;

    @Value("${openai.api.url}")
    private String OPENAI_URL;

    public TextAiResponseDTO requestGpt(String prompt) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(gptConfig.getApiKey());
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        GptRequestDTO requestDTO = GptRequestDTO.builder()
                .model(gptConfig.getModel())
                .messages(List.of(new GptRequestDTO.Message("user", prompt)))
                .build();

        HttpEntity<GptRequestDTO> request = new HttpEntity<>(requestDTO, httpHeaders);
//        ResponseEntity<OpenAiResponse> response = restTemplate.postForEntity(
//                OPENAI_URL, request, OpenAiResponse.class
//        );
        try {
            ResponseEntity<OpenAiResponse> response = restTemplate.postForEntity(
                    OPENAI_URL, request, OpenAiResponse.class
            );
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                OpenAiResponse body = response.getBody();

                if (body.choices != null && !body.choices.isEmpty()) {
                    String content = body.choices.get(0).message.content;
                    System.out.println("response content : " + content);

                    // content 안에 JSON 문자열 파싱
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        return objectMapper.readValue(content, TextAiResponseDTO.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException("GPT content JSON 파싱 실패", e);
                    }
                } else {
                    throw new RuntimeException("GPT choices 비어있음");
                }
            } else {
                throw new RuntimeException("OpenAI 호출 실패: " + response.getStatusCode());
            }

            // ... 기존 코드
        } catch (HttpClientErrorException e) {
            log.error("OpenAI API 호출 실패. 상태 코드: {}, 응답 본문: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("OpenAI API 호출 실패", e);
        } catch (Exception e) {
            log.error("예상치 못한 오류 발생: {}", e.getMessage());
            throw new RuntimeException("예상치 못한 오류 발생", e);
        }


    }
}
