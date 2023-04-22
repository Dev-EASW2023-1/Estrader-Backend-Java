package com.example.demo.app.domain.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class FcmMessageDto {
    private Message message;

    @Builder
    public FcmMessageDto(Message message){
        this.message = message;
    }

    @Getter
    @Builder
    public static class Message {
        private Notification notification;
        private Data data;
        private String token;
    }

    @Getter
    @Builder
    public static class Notification {
        private String title;
        private String body;
    }

    @Getter
    @Builder
    public static class Data {
        private String userId;
        private String targetId;
        private String itemImage;
        private String phase;
    }
}