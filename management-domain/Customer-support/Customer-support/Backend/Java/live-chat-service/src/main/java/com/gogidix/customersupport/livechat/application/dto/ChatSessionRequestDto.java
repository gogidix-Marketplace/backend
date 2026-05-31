package com.gogidix.customersupport.livechat.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatSessionRequestDto {

    @NotBlank(message = "Customer ID is required")
    private String customerId;

    @NotBlank(message = "Customer name is required")
    private String customerName;

    @NotBlank(message = "Customer email is required")
    @Email(message = "Invalid email format")
    private String customerEmail;

    private String channel;

    private List<String> tags;

    private String category;

    private String relatedTicketId;

    private PreChatSurveyDataDto preChatSurveyData;

    private String ipAddress;

    private String userAgent;

    private String referrer;

    private String location;

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
}
