package com.choogoomoneyna.choogoomoneyna_be.account.codef.service;

import com.choogoomoneyna.choogoomoneyna_be.account.codef.mapper.CodefTokenMapper;
import com.choogoomoneyna.choogoomoneyna_be.account.codef.vo.CodefTokenVO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CodefTokenManager {

    private static final String OAUTH_TOKEN_URL = "https://oauth.codef.io/oauth/token";
    private static final String GRANT_TYPE = "client_credentials";
    private static final String SCOPE = "read";

    @Value("${codef.client_id}")
    protected String clientId;

    @Value("${codef.client_secret}")
    protected String clientSecret;

    private final CodefTokenMapper codefTokenMapper;
    private String accessToken;
    private long tokenExpiryTime;

    //mapper에서 codef access_token 가져오기
    public String getAccessToken() {

        CodefTokenVO latestToken = codefTokenMapper.getLatestToken();

        //토큰이 유효하면 vo getter로 access_token 추출
        if (latestToken!=null) {
            accessToken = latestToken.getAccessToken();
            tokenExpiryTime = latestToken.getTokenExpiryTime().getTime();
        }

        //토큰이 없거나 만료된 경우 재발급
        if (latestToken == null || isAccessTokenExpired()) {
            refreshAccessToken();
        }
        return accessToken;
    }

    //토큰 기한 만료 체크
    protected boolean isAccessTokenExpired() {
        return System.currentTimeMillis() >= tokenExpiryTime;
    }


    //access_token 재발급
    protected void refreshAccessToken() {

        HashMap<String, String> tokenMap = requestAccessToken();
        System.out.println("✅ clientId: " + clientId);
        System.out.println("✅ clientSecret: " + clientSecret);

        if(tokenMap!=null && tokenMap.containsKey("access_token")) {
            accessToken = tokenMap.get("access_token");
            tokenExpiryTime = System.currentTimeMillis() + 3600 * 1000 * 24 * 7;

            CodefTokenVO codefTokenVO = new CodefTokenVO();
            codefTokenVO.setAccessToken(accessToken);
            codefTokenVO.setTokenExpiryTime(new Timestamp(tokenExpiryTime));

            //db에 토큰 저장 또는 업데이트
            CodefTokenVO existToken = codefTokenMapper.getLatestToken();
            if(existToken != null) {
                codefTokenVO.setId(existToken.getId());
                codefTokenMapper.updateToken(codefTokenVO);
            } else {
                codefTokenMapper.insertToken(codefTokenVO);
            }
        } else {
            throw new RuntimeException("토큰 발급에 실패하였습니다(failed get token)");
        }
    }

    //codef api에 access_token 발급 요청
    protected HashMap<String, String> requestAccessToken() {
        try {
            URL url = new URL(OAUTH_TOKEN_URL);
            //String params = "grant_type=" + GRANT_TYPE + "&scope=" + SCOPE;
            String params = "grant_type=client_credentials";

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            String auth = clientId + ":" + clientSecret;
            String authHeader =  "Basic " + Base64.getEncoder().encodeToString(auth.getBytes());
            connection.setRequestProperty("Authorization", authHeader);
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(params.getBytes());
                os.flush();
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try(BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String inputLine;
                    while ((inputLine = br.readLine()) != null) {
                        response.append(inputLine);
                    }
                    System.out.println("Error Body: " + response);
                    ObjectMapper mapper = new ObjectMapper();
                    return mapper.readValue(response.toString(), new TypeReference<HashMap<String, String>>(){});
                }
            } else {
                System.out.println("Error: HTTP response code" + responseCode);
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
