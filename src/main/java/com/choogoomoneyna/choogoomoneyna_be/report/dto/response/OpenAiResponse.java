package com.choogoomoneyna.choogoomoneyna_be.report.dto.response;

import java.util.List;

public class OpenAiResponse {
    public List<Choice> choices;

    public static class Choice {
        public Message message;
    }

    public static class Message {
        public String role;
        public String content;  // JSON 문자열이 여기에 있음
    }
}

