package com.choogoomoneyna.choogoomoneyna_be.account.codef.service.mock;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.nio.charset.StandardCharsets;


public class CustomHttpClient {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String MOCK_BASE =  "src/main/resources/mock-data/";

    public HttpResponse<String> send(HttpRequest request) throws IOException, InterruptedException {
        System.out.println("Received mock header: " + request.headers().firstValue("X-MOCK-SCENARIO"));

        Optional<String> mockHeader = request.headers().firstValue("X-MOCK-SCENARIO");

        //1. 외부 요청(CODEF 서버)
//         HttpResponse<String> realResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
//         String responseBody = realResponse.body();

        //2. 목데이터
        String scenarioName = mockHeader.orElse("default-real-response");

        // 클래스패스에서 mock json 읽기
        InputStream is = getClass().getClassLoader().getResourceAsStream("mock-data/" + scenarioName + ".json");
        if (is == null) {
            throw new FileNotFoundException("mock-data/" + scenarioName + ".json not found");
        }
        //Path filePath = Path.of(MOCK_BASE + scenarioName + ".json");
        //String responseBody = Files.readString(filePath);

        String responseBody = new String(is.readAllBytes(), StandardCharsets.UTF_8);

        //3. 헤더값에 따라 응답 설정
        if (mockHeader.isPresent()) {
            responseBody = overrideResponse(responseBody, scenarioName);
        }

        return new MockHttpResponse(responseBody,200);
    }

//    public HttpResponse<String> send(HttpRequest request) throws IOException, InterruptedException {
//        Optional<String> mockHeader = request.headers().firstValue("X-MOCK-SCENARIO");
//        System.out.println("Received mock header in custom send: " + mockHeader);
//
//        if (mockHeader.isPresent()) {
//            // 🎯 1. 목 응답 처리
//            String scenarioName = mockHeader.get();
//            InputStream is = getClass().getClassLoader().getResourceAsStream("mock-data/" + scenarioName + ".json");
//
//            if (is == null) {
//                throw new FileNotFoundException("mock-data/" + scenarioName + ".json not found");
//            }
//
//            String responseBody = new String(is.readAllBytes(), StandardCharsets.UTF_8);
//            responseBody = overrideResponse(responseBody, scenarioName);  // 커넥티드아이디 추가 등 시나리오 대응
//
//            return new MockHttpResponse(responseBody, 200);
//
//        } else {
//            // ✅ 2. 실서버로 직접 요청 보내기
//            HttpClient realClient = HttpClient.newHttpClient();
//            return realClient.send(request, HttpResponse.BodyHandlers.ofString());
//        }
//    }


    private String overrideResponse(String originalJson, String scenario) throws IOException {
        JsonNode root = objectMapper.readTree(originalJson);

        // 루트가 배열이고, 최소 1개 이상 항목이 있을 때만 진행
        if (root.isArray() && root.size() > 0) {
            JsonNode firstNode = root.get(0);

            if (firstNode.isObject()) {
                ObjectNode objectNode = (ObjectNode) firstNode;

                switch (scenario) {
                    case "mock-account-success-kb":
                        objectNode.put("connectedId", "mock-connected-id-kb");
                        break;

                    case "default-real-response":
                        objectNode.put("connectedId", "mock-connected-id-toss");
                        break;

                    case "mock-account-success-sh":
                        objectNode.put("connectedId", "mock-connected-id-shinhan");
                        break;

                    // 필요 시 다른 시나리오 추가
                }
            }
        }

        return objectMapper.writeValueAsString(root);
    }


}
