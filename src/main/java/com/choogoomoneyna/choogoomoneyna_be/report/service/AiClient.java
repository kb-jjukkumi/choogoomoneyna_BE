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
