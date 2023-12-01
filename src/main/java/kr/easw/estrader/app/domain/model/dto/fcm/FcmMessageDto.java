package kr.easw.estrader.app.domain.model.dto.fcm;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
        private Android android;
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

    @Getter
    @Builder
    public static class Android {
        private String priority;
    }
}