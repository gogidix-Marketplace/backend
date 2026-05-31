package com.gogidix.sales.notification.application.dto.response;

import com.gogidix.sales.notification.domain.valueobject.NotificationChannel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

/**
 * Notification Template Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationTemplateResponseDto {

    private String id;
    private String templateId;
    private String tenantId;
    private String code;
    private String name;
    private String description;
    private NotificationChannel channel;
    private String subjectTemplate;
    private String contentTemplate;
    private String htmlContentTemplate;
    private Map<String, TemplateVariableDto> variables;
    private String locale;
    private Boolean isActive;
    private Integer version;
    private Instant validFrom;
    private Instant validUntil;
    private String tags;
    private Instant createdAt;
    private Instant updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TemplateVariableDto {
        private String name;
        private String type;
        private String description;
        private Boolean required;
        private String defaultValue;
    }
}
