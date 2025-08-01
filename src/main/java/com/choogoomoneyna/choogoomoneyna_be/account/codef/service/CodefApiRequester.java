package com.choogoomoneyna.choogoomoneyna_be.account.codef.service;

import com.choogoomoneyna.choogoomoneyna_be.account.codef.dto.*;
import com.choogoomoneyna.choogoomoneyna_be.account.codef.service.mock.CustomHttpClient;
import com.choogoomoneyna.choogoomoneyna_be.account.db.mapper.AccountMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CodefApiRequester {

    @Value("${codef.RSA_public_key}")
    String PUBLIC_KEY;

    private final CodefTokenManager codefTokenManager;
    private final AccountMapper accountMapper;

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
            if (errorMessage.contains("이미 계정이 등록된 기관")) {
                log.warn("이미 등록된 기관이므로 addConnectedId는 생략합니다.");
                return connectedId;
            }
            //throw new Exception(errorMessage);
        }

        return connectedId;
    }


    protected String extractErrorMessage(String response) throws Exception {
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
                return "already added account sxists.";  // 커스텀 메시지
            }

            // 기본 메시지 반환
            return errorMessage;
        }

        return null;  // 에러가 없으면 null 반환
    }

    // ConnectedId를 API 요청으로부터 추출하는 메서드
    protected String extractConnectedIdFromResponse(String response) throws IOException {
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
    protected String buildRequestBody(AccountRequestDto accountRequestDto) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode accountInfo = buildAccountInfo(accountRequestDto);

        ArrayNode accountList = mapper.createArrayNode();
        accountList.add(accountInfo);

        ObjectNode requestBody = mapper.createObjectNode();
        requestBody.set("accountList", accountList);

        return requestBody.toString();
    }

    // 요청 바디 생성(Overload, connectedid있는 경우)
    protected String buildRequestBody(AccountRequestDto accountRequestDto, String connectedId) {
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
    protected ObjectNode buildAccountInfo(AccountRequestDto accountRequestDto) {
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

    // 모든 api 요청 전송 메서드
    protected String sendPostRequest(String requestUrl, String accessToken, String requestBody, String mockScenario) throws IOException, InterruptedException {

        //HttpClient client = HttpClient.newHttpClient();

        //mock
        CustomHttpClient client = new CustomHttpClient();

        log.info("requester sendPostRequest {}", mockScenario);
        System.out.println("requester sendPostRequest {}"+ mockScenario);

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(requestUrl))
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody));

        if (mockScenario != null) {
            builder.header("X-MOCK-SCENARIO", mockScenario);
        }

        HttpRequest request = builder.build(); // 🔒 반드시 build 이후 request 완성

        //mock
        HttpResponse<String> response = client.send(request);

        if (response.statusCode() == 200) {
            // 응답을 URL 디코딩
            String decodedResponse = URLDecoder.decode(response.body(), "UTF-8");
            return decodedResponse;
        } else {
            throw new IOException("HTTP error code: " + response.statusCode());
        }
    }


    protected String sendPostRequest(String requestUrl, String accessToken, String requestBody) throws IOException, InterruptedException {

        //HttpClient client = HttpClient.newHttpClient();

        //mock
        CustomHttpClient client = new CustomHttpClient();

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(requestUrl))
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json");


        HttpRequest request = requestBuilder
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        //HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        //mock
        HttpResponse<String> response = client.send(request);

        if (response.statusCode() == 200) {
            // 응답을 URL 디코딩
            String decodedResponse = URLDecoder.decode(response.body(), "UTF-8");
            return decodedResponse;
        } else {
            throw new IOException("HTTP error code: " + response.statusCode());
        }
    }


    // 보유 계좌 조회 메서드
    public List<AccountResponseDto> getAccountList(Long userId, AccountRequestDto accountRequestDto, String connectedId, String mockScenario) throws Exception {
        String requestUrl = "https://development.codef.io/v1/kr/bank/p/account/account-list";

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode requestBody = mapper.createObjectNode();
        requestBody.put("organization", accountRequestDto.getBankId());
        requestBody.put("connectedId", connectedId);

        log.info("requester layer getAccountList method {} ",mockScenario);
        String response = sendPostRequest(requestUrl, codefTokenManager.getAccessToken(), requestBody.toString(), mockScenario);

        List<AccountResponseDto> accountList = new ArrayList<>();
        JsonNode jsonResponse = mapper.readTree(response);

        // 응답에서 따로 메시지를 추출
        String message = jsonResponse.path("result").path("message").asText();

        JsonNode depositTrustList = jsonResponse.path("data").path("resDepositTrust");
        if (depositTrustList.isArray() && depositTrustList.size() > 0) {
            for (JsonNode accountInfo : depositTrustList) {
                AccountResponseDto accountResponseDto = new AccountResponseDto();
                String resAccount = (accountInfo.get("resAccount").asText());
                if(accountMapper.findByUserIdAndAccountNum(userId, resAccount)!=null){
                    continue;
                } else{
                    accountResponseDto.setAccountNum(resAccount);
                }
                accountResponseDto.setAccountBalance(accountInfo.get("resAccountBalance").asText());
                accountResponseDto.setAccountName(accountInfo.get("resAccountName").asText());
                accountResponseDto.setBankId(accountRequestDto.getBankId());

                accountList.add(accountResponseDto);
            }
        }
        return accountList;
    }

    // 계좌 하나 업데이트 메서드
    public List<AccountResponseDto> getAccountOne(String bankId, String connectedId) throws Exception {
        String requestUrl = "https://development.codef.io/v1/kr/bank/p/account/account-list";

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode requestBody = mapper.createObjectNode();
        requestBody.put("organization", bankId);
        requestBody.put("connectedId", connectedId);

        String response = sendPostRequest(requestUrl, codefTokenManager.getAccessToken(), requestBody.toString());

        List<AccountResponseDto> accountList = new ArrayList<>();
        JsonNode jsonResponse = mapper.readTree(response);

        // 응답 메시지 확인 (디버깅용)
        String message = jsonResponse.path("result").path("message").asText();
        log.info("CODEF 응답 메시지: {}", message);
        log.info("CODEF 전체 응답: {}", jsonResponse.toPrettyString());

        // 계좌 목록 추출
        JsonNode depositTrustList = jsonResponse.path("data").path("resDepositTrust");
        if (depositTrustList.isArray() && depositTrustList.size() > 0) {
            for (JsonNode accountInfo : depositTrustList) {
                AccountResponseDto accountResponseDto = new AccountResponseDto();
                accountResponseDto.setAccountNum(accountInfo.get("resAccount").asText());
                accountResponseDto.setAccountBalance(accountInfo.get("resAccountBalance").asText());
                accountResponseDto.setAccountName(accountInfo.get("resAccountName").asText());
                accountResponseDto.setBankId(bankId);

                accountList.add(accountResponseDto);
            }
        }

        return accountList;
    }


    public CodefTransactionResponseDto getTransactionList(TransactionRequestDto requestDto) throws Exception {
        try {
            String accessToken = codefTokenManager.getAccessToken();
            String requestUrl = "https://development.codef.io/v1/kr/bank/p/account/transaction-list";

            // 요청 바디 생성
            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(requestDto);

            // API 요청 보내기
            String response = sendPostRequest(requestUrl, accessToken, requestBody);

            // 응답 파싱
            JsonNode jsonNode = objectMapper.readTree(response);

            // 결과 코드 체크
            String resultCode = jsonNode.path("result").path("code").asText();
            if (!"CF-00000".equals(resultCode)) {
                throw new Exception("Codef API error: " + jsonNode.path("result").path("message").asText());
            }

            // TransactionResponseDto 생성
            //TransactionResponseDto transactionResponseDto = new TransactionResponseDto();
            CodefTransactionResponseDto codefTransactionResponseDto = new CodefTransactionResponseDto();
            codefTransactionResponseDto.setAccountNum(jsonNode.path("data").path("resAccount").asText());

            // 거래 내역 리스트 존재 여부 체크
            ArrayNode transactionArray = (ArrayNode) jsonNode.path("data").path("resTrHistoryList");
            List<CodefTransactionResponseDto.HistoryItem> transactionList = new ArrayList<>();

            // 각 항목을 DTO로 변환하여 리스트에 추가
            for (JsonNode node : transactionArray) {
                CodefTransactionResponseDto.HistoryItem trItem = objectMapper.treeToValue(node, CodefTransactionResponseDto.HistoryItem.class);
                transactionList.add(trItem);
            }

            codefTransactionResponseDto.setResTrHistoryList(transactionList);
            return codefTransactionResponseDto;

        } catch (Exception e) {
            // 에러가 발생하면 로그를 남기고 null 반환
            log.error("error occur in transaction get: ", e);
            return null; // 에러 발생 시 null 반환
        }
    }


}
