package com.gogidix.sales.leadmanagement.domain.port.in;

import com.gogidix.sales.leadmanagement.domain.model.Lead;
import com.gogidix.sales.leadmanagement.domain.model.LeadActivity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Lead Commands (Input Port)
 * Defines the input commands for lead operations
 */
public interface LeadCommand {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CreateLeadCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        private String firstName;

        private String lastName;

        @Email(message = "Invalid email format")
        private String email;

        private String phone;

        private String mobilePhone;

        private String company;

        private String title;

        private String industry;

        private String companySize;

        private String website;

        private String linkedInUrl;

        @NotNull(message = "Source is required")
        private Lead.LeadSource source;

        private String sourceDetails;

        private String campaign;

        private String territory;

        private String region;

        private String segment;

        private Integer budget;

        private Integer authority;

        private Integer need;

        private Integer timeline;

        private String ownerId;

        private String ownerName;

        private BigDecimal estimatedValue;

        private String currency;

        private LocalDate expectedCloseDate;

        private String notes;

        private List<String> tags;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateLeadCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Lead ID is required")
        private String leadId;

        private String firstName;

        private String lastName;

        private String email;

        private String phone;

        private String mobilePhone;

        private String company;

        private String title;

        private String industry;

        private String companySize;

        private String website;

        private String linkedInUrl;

        private String territory;

        private String region;

        private String segment;

        private Integer budget;

        private Integer authority;

        private Integer need;

        private Integer timeline;

        private BigDecimal estimatedValue;

        private String currency;

        private LocalDate expectedCloseDate;

        private String notes;

        private List<String> tags;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class AssignLeadCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Lead ID is required")
        private String leadId;

        @NotBlank(message = "Owner ID is required")
        private String ownerId;

        private String ownerName;

        private String reason;

        private String assignmentStrategy;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class AdvanceStageCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Lead ID is required")
        private String leadId;

        private String notes;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class RegressStageCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Lead ID is required")
        private String leadId;

        @NotNull(message = "Target stage is required")
        private Lead.LeadStage targetStage;

        @NotBlank(message = "Reason is required")
        private String reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ConvertLeadCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Lead ID is required")
        private String leadId;

        private String dealId;

        private String reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class MarkAsLostCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Lead ID is required")
        private String leadId;

        @NotBlank(message = "Loss reason is required")
        private String lossReason;

        private String lossDetails;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateScoreCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Lead ID is required")
        private String leadId;

        @NotNull(message = "Score is required")
        private Integer score;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class AddActivityCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Lead ID is required")
        private String leadId;

        @NotNull(message = "Activity type is required")
        private LeadActivity.ActivityType activityType;

        @NotBlank(message = "Subject is required")
        private String subject;

        private String description;

        private java.time.Instant dueDate;

        private LeadActivity.Priority priority;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CompleteActivityCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Activity ID is required")
        private String activityId;

        private String outcome;

        private Integer durationMinutes;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class RecordInteractionCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Lead ID is required")
        private String leadId;

        @NotNull(message = "Interaction type is required")
        private InteractionType interactionType;

        private Boolean emailOpened;

        private Boolean emailClicked;

        private String formName;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class RecycleLeadCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Lead ID is required")
        private String leadId;

        private String reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class DeleteLeadCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Lead ID is required")
        private String leadId;
    }

    enum InteractionType {
        EMAIL_OPEN,
        EMAIL_CLICK,
        WEB_VISIT,
        FORM_SUBMIT
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class LeadActivityCommand {
        private LeadActivity.ActivityType activityType;
        private String subject;
        private String description;
        private java.time.Instant dueDate;
        private LeadActivity.Priority priority;
    }
}
