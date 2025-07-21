package com.choogoomoneyna.choogoomoneyna_be.account.codef.service;

import com.choogoomoneyna.choogoomoneyna_be.account.codef.dto.AccountRequestDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class CodefApiRequester {

    @Value("${codef.RSA_public_key}")
    String PUBLIC_KEY;

    private final CodefTokenManager codefTokenManager;
    private final SqlSessionTemplate sqlSessionTemplate;

    //커넥티드 아이디 계정 등록
    public String registerConnectedId(AccountRequestDto accountRequestDto) throws Exception {
        //access_token
        String accessToken = codefTokenManager.getAccessToken();

        //요청 api 경로
        String requestUrl = "https://development.codef.io/v1/account/create";

        //커넥티드 아이디 등록 요청 바디 생성
        String requestBody = buildRequestBody(accountRequestDto);

        //api 요청 보내기
        String response = sendPostRequest(requestUrl, accessToken, requestBody);
        log.info(response);

        String errorMessage = extractErrorMessage(response);
        if(errorMessage != null) {
            throw new Exception(errorMessage);
        }

        return extractConnectedIdFromResponse(response);
    }

    //커넥티드 아이디 계정 추가(이미 커넥티드 아이디가 있는 유저가 새로운 기관의 계정 정보를 추가하는 경우)
    public String addConnectedId(String connectedId, AccountRequestDto accountRequestDto) throws Exception {

        String accessToken = codefTokenManager.getAccessToken();
        String requestUrl = "https://development.codef.io/v1/account/add";

        String requestBody = buildRequestBody(accountRequestDto, connectedId);
        String response = sendPostRequest(requestUrl, accessToken, requestBody);
        log.info(response);

        // 에러 메시지 추출
        String errorMessage = extractErrorMessage(response);
        if (errorMessage != null) {
            throw new Exception(errorMessage);  // 에러가 있으면 예외 발생
        }

        return connectedId;
    }

    private String extractErrorMessage(String response) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonResponse = mapper.readTree(response);

        // errorList 확인
        JsonNode errorList = jsonResponse.path("data").path("errorList");
        if (errorList.isArray() && errorList.size() > 0) {
            JsonNode errorDetail = errorList.get(0);  // 첫 번째 에러만 처리
            String errorCode = errorDetail.path("code").asText();
            String errorMessage = errorDetail.path("message").asText();

            // 특정 에러 코드에 대한 커스텀 메시지 처리
            if ("CF-04004".equals(errorCode)) {
                return "기존에 연결한 계좌가 존재합니다.";  // 커스텀 메시지
            }

            // 기본 메시지 반환
            return errorMessage;
        }

        return null;  // 에러가 없으면 null 반환
    }

    // ConnectedId를 API 요청으로부터 추출하는 메서드
    private String extractConnectedIdFromResponse(String response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = (ObjectNode) mapper.readTree(response);

        // "data" 객체 안에 있는 "connectedId" 필드 추출
        if (rootNode.has("data") && rootNode.get("data").has("connectedId")) {
            return rootNode.get("data").get("connectedId").asText();
        } else {
            throw new IOException("connectedId not found in response");
        }
    }

    // 요청 바디 생성
    private String buildRequestBody(AccountRequestDto accountRequestDto) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode accountInfo = buildAccountInfo(accountRequestDto);

        ArrayNode accountList = mapper.createArrayNode();
        accountList.add(accountInfo);

        ObjectNode requestBody = mapper.createObjectNode();
        requestBody.set("accountList", accountList);

        return requestBody.toString();
    }

    // 요청 바디 생성(Overload, connectedid있는 경우)
    private String buildRequestBody(AccountRequestDto accountRequestDto, String connectedId) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode accountInfo = buildAccountInfo(accountRequestDto);

        ArrayNode accountList = mapper.createArrayNode();
        accountList.add(accountInfo);

        ObjectNode requestBody = mapper.createObjectNode();
        requestBody.set("accountList", accountList);
        requestBody.put("connectedId", connectedId);  // connectedId 추가

        return requestBody.toString();
    }

    // accountInfo 객체 생성
    private ObjectNode buildAccountInfo(AccountRequestDto accountRequestDto) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode accountInfo = mapper.createObjectNode();

        accountInfo.put("countryCode", "KR");
        accountInfo.put("businessType", "BK");
        accountInfo.put("clientType", "P");
        accountInfo.put("organization", accountRequestDto.getBankId());
        accountInfo.put("loginType", "1");
        accountInfo.put("id", accountRequestDto.getUserBankId());

        // 암호화된 비밀번호 추가
        String encryptedPassword = RSAUtil.encryptRSA(accountRequestDto.getUserBankPassword(), PUBLIC_KEY);
        accountInfo.put("password", encryptedPassword);

        return accountInfo;
    }

    // 커넥티드 아이디 등록,추가 api 요청 전송
    private String sendPostRequest(String requestUrl, String accessToken, String requestBody) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(requestUrl))
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            // 응답을 URL 디코딩
            String decodedResponse = URLDecoder.decode(response.body(), "UTF-8");
            return decodedResponse;
        } else {
            throw new IOException("HTTP error code: " + response.statusCode());
        }
    }



}
