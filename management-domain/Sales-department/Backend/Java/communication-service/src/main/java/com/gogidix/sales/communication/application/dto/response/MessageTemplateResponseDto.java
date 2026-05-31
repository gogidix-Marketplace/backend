package com.gogidix.sales.communication.application.dto.response;

import com.gogidix.sales.communication.domain.model.Message;
import com.gogidix.sales.communication.domain.model.MessageTemplate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

/**
 * Message Template Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageTemplateResponseDto {

    private String id;
    private String templateId;
    private String tenantId;
    private String name;
    private String description;
    private String code;
    private ChannelTypeDto channelType;
    private TemplateTypeDto type;
    private String subject;
    private String content;
    private String htmlContent;
    private List<TemplateVariableDto> variables;
    private TemplateStatusDto status;
    private String category;
    private List<String> tags;
    private String language;
    private String locale;
    private Integer version;
    private String parentTemplateId;
    private Boolean isSystemTemplate;
    private Long usageCount;
    private Instant lastUsedAt;
    private Instant createdAt;
    private Instant updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TemplateVariableDto {
        private String name;
        private String description;
        private VariableTypeDto type;
        private Boolean isRequired;
        private String defaultValue;
    }

    public enum ChannelTypeDto {
        EMAIL,
        SMS,
        IN_APP,
        WHATSAPP,
        PUSH_NOTIFICATION
    }

    public enum TemplateTypeDto {
        MARKETING,
        TRANSACTIONAL,
        NOTIFICATION,
        ALERT,
        WELCOME,
        PASSWORD_RESET,
        ORDER_CONFIRMATION,
        SHIPPING_NOTIFICATION,
        INVOICE,
        REMINDER,
        PROMOTIONAL,
        NEWSLETTER,
        SURVEY,
        SUPPORT
    }

    public enum TemplateStatusDto {
        DRAFT,
        ACTIVE,
        INACTIVE,
        ARCHIVED
    }

    public enum VariableTypeDto {
        STRING,
        NUMBER,
        DATE,
        BOOLEAN,
        URL,
        EMAIL,
        LIST
    }

    public static ChannelTypeDto mapChannelType(Message.ChannelType channelType) {
        return channelType != null ? ChannelTypeDto.valueOf(channelType.name()) : null;
    }

    public static TemplateTypeDto mapTemplateType(MessageTemplate.TemplateType type) {
        return type != null ? TemplateTypeDto.valueOf(type.name()) : null;
    }

    public static TemplateStatusDto mapTemplateStatus(MessageTemplate.TemplateStatus status) {
        return status != null ? TemplateStatusDto.valueOf(status.name()) : null;
    }

    public static VariableTypeDto mapVariableType(MessageTemplate.TemplateVariable.VariableType type) {
        return type != null ? VariableTypeDto.valueOf(type.name()) : null;
    }
}
