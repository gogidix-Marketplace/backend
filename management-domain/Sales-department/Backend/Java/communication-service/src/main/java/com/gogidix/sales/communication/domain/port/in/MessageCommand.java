package com.gogidix.sales.communication.domain.port.in;

import com.gogidix.sales.communication.domain.model.Message;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

/**
 * Message Commands (Input Port)
 * Defines the input commands for message operations
 */
public interface MessageCommand {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CreateMessageCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        private String conversationId;

        @NotBlank(message = "Sender ID is required")
        private String senderId;

        private String senderName;

        @NotEmpty(message = "At least one recipient is required")
        private List<Message.RecipientInfo> recipients;

        @NotNull(message = "Channel is required")
        private Message.ChannelType channel;

        private String subject;

        @NotBlank(message = "Content is required")
        private String content;

        private String templateId;

        private List<Message.Attachment> attachments;

        private Integer priority;

        private Instant scheduledAt;

        private String parentMessageId;

        private String relatedEntityType;

        private String relatedEntityId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class SendMessageCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Message ID is required")
        private String messageId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class MarkAsReadCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Message ID is required")
        private String messageId;

        @NotBlank(message = "User ID is required")
        private String userId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ScheduleMessageCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Message ID is required")
        private String messageId;

        @NotNull(message = "Scheduled time is required")
        private Instant scheduledAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class DeleteMessageCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Message ID is required")
        private String messageId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class AddAttachmentCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Message ID is required")
        private String messageId;

        @NotNull(message = "Attachment is required")
        private Message.Attachment attachment;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class MarkAsDeliveredCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Message ID is required")
        private String messageId;

        private String externalMessageId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class MarkAsFailedCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Message ID is required")
        private String messageId;

        @NotBlank(message = "Error message is required")
        private String errorMessage;
    }
}
