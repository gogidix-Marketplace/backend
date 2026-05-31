package com.gogidix.sales.notification.domain.port.in;

import com.gogidix.sales.notification.domain.model.NotificationTemplate;
import com.gogidix.sales.notification.domain.valueobject.NotificationChannel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

/**
 * Notification Template Commands (Input Port)
 * Defines the input commands for template operations
 */
public interface NotificationTemplateCommand {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CreateTemplateCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Code is required")
        private String code;

        @NotBlank(message = "Name is required")
        private String name;

        private String description;

        @NotNull(message = "Channel is required")
        private NotificationChannel channel;

        private String subjectTemplate;

        @NotBlank(message = "Content template is required")
        private String contentTemplate;

        private String htmlContentTemplate;

        private Map<String, NotificationTemplate.TemplateVariable> variables;

        private String locale;

        private Instant validFrom;

        private Instant validUntil;

        private String tags;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateTemplateCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Template ID is required")
        private String templateId;

        private String name;

        private String description;

        private String subjectTemplate;

        private String contentTemplate;

        private String htmlContentTemplate;

        private Map<String, NotificationTemplate.TemplateVariable> variables;

        private String locale;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ActivateTemplateCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Template ID is required")
        private String templateId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class DeactivateTemplateCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Template ID is required")
        private String templateId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class DeleteTemplateCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Template ID is required")
        private String templateId;
    }
}
