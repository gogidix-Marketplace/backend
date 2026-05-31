package com.gogidix.customersupport.livechat.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gogidix.customersupport.livechat.domain.model.ChatSession;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatSessionResponseDto {

    private String id;
    private String tenantId;
    private String sessionId;
    private String customerId;
    private String customerName;
    private String customerEmail;
    private String assignedAgentId;
    private String assignedAgentName;
    private ChatStatusDto status;
    private String channel;
    private Integer queuePosition;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant startedAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant endedAt;

    private Long durationSeconds;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant firstResponseAt;

    private Long waitingTimeSeconds;

    private Integer rating;

    private String feedback;

    private List<String> tags;

    private String category;

    private String relatedTicketId;

    private Integer messageCount;

    private Integer customerMessageCount;

    private Integer agentMessageCount;

    private String ipAddress;

    private String userAgent;

    private String referrer;

    private String location;

    private PreChatSurveyDataDto preChatSurveyData;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant updatedAt;

    public enum ChatStatusDto {
        WAITING, ACTIVE, ENDED, TRANSFERRED, TIMED_OUT
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PreChatSurveyDataDto {
        private String question1;
        private String answer1;
        private String question2;
        private String answer2;
        private String question3;
        private String answer3;
    }

    public static ChatStatusDto fromEntityStatus(ChatSession.ChatStatus status) {
        return ChatStatusDto.valueOf(status.name());
    }
}
