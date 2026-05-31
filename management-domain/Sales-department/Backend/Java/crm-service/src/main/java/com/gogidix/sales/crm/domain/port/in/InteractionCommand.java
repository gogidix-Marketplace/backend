package com.gogidix.sales.crm.domain.port.in;

import com.gogidix.sales.crm.domain.model.Interaction;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Interaction Commands (Input Port)
 * Defines the input commands for interaction operations
 */
public interface InteractionCommand {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CreateInteractionCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Customer ID is required")
        private String customerId;

        private String customerName;

        private String contactId;

        private String contactName;

        @NotNull(message = "Interaction type is required")
        private Interaction.InteractionType type;

        @NotNull(message = "Interaction direction is required")
        private Interaction.InteractionDirection direction;

        @NotNull(message = "Interaction date is required")
        private LocalDateTime interactionDate;

        @NotBlank(message = "Subject is required")
        @Size(min = 2, max = 255, message = "Subject must be between 2 and 255 characters")
        private String subject;

        private String description;

        private String location;

        private String assignedTo;

        private String assignedToName;

        private String campaignId;

        private String dealId;

        private Double dealValue;

        private Integer probability;

        private Boolean isHighPriority;

        private String notes;

        private List<String> participantContactIds;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CompleteInteractionCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Interaction ID is required")
        private String interactionId;

        private String outcome;

        private String notes;

        @Positive(message = "Duration must be positive")
        private Integer durationMinutes;

        private LocalDate followUpDate;

        private String followUpNotes;

        private String nextStep;

        private LocalDate nextStepDate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CancelInteractionCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Interaction ID is required")
        private String interactionId;

        private String reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class RescheduleInteractionCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Interaction ID is required")
        private String interactionId;

        @NotNull(message = "New date is required")
        @FutureOrPresent(message = "New date must be in the future or present")
        private LocalDateTime newDate;

        private String reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class AddParticipantCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Interaction ID is required")
        private String interactionId;

        @NotBlank(message = "Contact ID is required")
        private String contactId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class AddAttachmentCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Interaction ID is required")
        private String interactionId;

        @NotBlank(message = "Attachment URL is required")
        private String attachmentUrl;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class AssociateWithDealCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Interaction ID is required")
        private String interactionId;

        private String dealId;

        private Double dealValue;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class MarkAsHighPriorityCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Interaction ID is required")
        private String interactionId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class DeleteInteractionCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Interaction ID is required")
        private String interactionId;
    }
}
