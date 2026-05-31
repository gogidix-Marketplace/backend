package com.gogidix.shared.infrastructure.services.communication.notification.application.dto.request;

import com.gogidix.shared.infrastructure.services.communication.notification.domain.model.Notification;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Send notification request DTO.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendNotificationRequestDto {

    @NotBlank(message = "User ID is required")
    private String userId;

    @NotNull(message = "Type is required")
    private Notification.NotificationType type;

    @NotNull(message = "Channel is required")
    private Notification.NotificationChannel channel;

    @NotBlank(message = "Recipient is required")
    private String recipient;

    private String subject;

    @NotBlank(message = "Body is required")
    private String body;

    private String templateId;

    private Map<String, Object> templateVariables;
}
