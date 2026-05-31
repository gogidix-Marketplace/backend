package com.gogidix.customersupport.livechat.application.mapper;

import com.gogidix.customersupport.livechat.application.dto.ChatSessionRequestDto;
import com.gogidix.customersupport.livechat.application.dto.ChatSessionResponseDto;
import com.gogidix.customersupport.livechat.domain.model.ChatSession;
import org.springframework.stereotype.Component;

@Component
public class ChatSessionMapper {

    public ChatSession toEntity(ChatSessionRequestDto dto, String tenantId) {
        ChatSession session = ChatSession.create(
                tenantId,
                dto.getCustomerId(),
                dto.getCustomerName(),
                dto.getCustomerEmail()
        );

        session.setChannel(dto.getChannel());
        session.setTags(dto.getTags());
        session.setCategory(dto.getCategory());
        session.setRelatedTicketId(dto.getRelatedTicketId());
        session.setIpAddress(dto.getIpAddress());
        session.setUserAgent(dto.getUserAgent());
        session.setReferrer(dto.getReferrer());
        session.setLocation(dto.getLocation());

        if (dto.getPreChatSurveyData() != null) {
            session.setPreChatSurveyData(ChatSession.PreChatSurveyData.builder()
                    .question1(dto.getPreChatSurveyData().getQuestion1())
                    .answer1(dto.getPreChatSurveyData().getAnswer1())
                    .question2(dto.getPreChatSurveyData().getQuestion2())
                    .answer2(dto.getPreChatSurveyData().getAnswer2())
                    .question3(dto.getPreChatSurveyData().getQuestion3())
                    .answer3(dto.getPreChatSurveyData().getAnswer3())
                    .build());
        }

        return session;
    }

    public ChatSessionResponseDto toResponseDto(ChatSession entity) {
        ChatSessionResponseDto.PreChatSurveyDataDto surveyDataDto = null;
        if (entity.getPreChatSurveyData() != null) {
            surveyDataDto = ChatSessionResponseDto.PreChatSurveyDataDto.builder()
                    .question1(entity.getPreChatSurveyData().getQuestion1())
                    .answer1(entity.getPreChatSurveyData().getAnswer1())
                    .question2(entity.getPreChatSurveyData().getQuestion2())
                    .answer2(entity.getPreChatSurveyData().getAnswer2())
                    .question3(entity.getPreChatSurveyData().getQuestion3())
                    .answer3(entity.getPreChatSurveyData().getAnswer3())
                    .build();
        }

        return ChatSessionResponseDto.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .sessionId(entity.getSessionId())
                .customerId(entity.getCustomerId())
                .customerName(entity.getCustomerName())
                .customerEmail(entity.getCustomerEmail())
                .assignedAgentId(entity.getAssignedAgentId())
                .assignedAgentName(entity.getAssignedAgentName())
                .status(ChatSessionResponseDto.fromEntityStatus(entity.getStatus()))
                .channel(entity.getChannel())
                .queuePosition(entity.getQueuePosition())
                .startedAt(entity.getStartedAt())
                .endedAt(entity.getEndedAt())
                .durationSeconds(entity.getDurationSeconds())
                .firstResponseAt(entity.getFirstResponseAt())
                .waitingTimeSeconds(entity.getWaitingTimeSeconds())
                .rating(entity.getRating())
                .feedback(entity.getFeedback())
                .tags(entity.getTags())
                .category(entity.getCategory())
                .relatedTicketId(entity.getRelatedTicketId())
                .messageCount(entity.getMessageCount())
                .customerMessageCount(entity.getCustomerMessageCount())
                .agentMessageCount(entity.getAgentMessageCount())
                .ipAddress(entity.getIpAddress())
                .userAgent(entity.getUserAgent())
                .referrer(entity.getReferrer())
                .location(entity.getLocation())
                .preChatSurveyData(surveyDataDto)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
