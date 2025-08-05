package com.choogoomoneyna.choogoomoneyna_be.report.dto.request;

import lombok.*;

import java.nio.Buffer;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GptRequestDTO {
    private String model;
    private List<Message> messages;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Message {
        private String role;
        private String content;
    }
}
