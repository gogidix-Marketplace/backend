package com.gogidix.sales.communication.domain.port.in;

import com.gogidix.sales.communication.domain.model.Conversation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

/**
 * Conversation Commands (Input Port)
 * Defines the input commands for conversation operations
 */
public interface ConversationCommand {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CreateConversationCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        private String title;

        @NotNull(message = "Conversation type is required")
        private Conversation.ConversationType type;

        @NotBlank(message = "Owner ID is required")
        private String ownerId;

        private String ownerName;

        private List<Conversation.ParticipantDetails> participants;

        @NotNull(message = "Default channel is required")
        private Conversation.ChannelType defaultChannel;

        private String description;

        private String relatedEntityType;

        private String relatedEntityId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class AddParticipantCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Conversation ID is required")
        private String conversationId;

        @NotNull(message = "Participant is required")
        private Conversation.ParticipantDetails participant;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class RemoveParticipantCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Conversation ID is required")
        private String conversationId;

        @NotBlank(message = "Participant ID is required")
        private String participantId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateConversationCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Conversation ID is required")
        private String conversationId;

        private String title;

        private String description;

        private String assignedTo;

        private Integer priority;

        private String relatedEntityType;

        private String relatedEntityId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class MarkAsReadCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Conversation ID is required")
        private String conversationId;

        @NotBlank(message = "User ID is required")
        private String userId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ArchiveCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Conversation ID is required")
        private String conversationId;

        @NotBlank(message = "Archived by is required")
        private String archivedBy;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ResolveCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Conversation ID is required")
        private String conversationId;

        @NotBlank(message = "Resolved by is required")
        private String resolvedBy;

        private String resolutionNotes;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class AssignCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Conversation ID is required")
        private String conversationId;

        @NotBlank(message = "Assigned to is required")
        private String assignedTo;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class AddTagCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Conversation ID is required")
        private String conversationId;

        @NotBlank(message = "Tag is required")
        private String tag;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class SetSlaDeadlineCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Conversation ID is required")
        private String conversationId;

        @NotNull(message = "Deadline is required")
        private Instant deadline;
    }
}
