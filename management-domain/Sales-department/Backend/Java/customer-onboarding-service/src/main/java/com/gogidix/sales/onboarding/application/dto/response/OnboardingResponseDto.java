package com.gogidix.sales.onboarding.application.dto.response;

import com.gogidix.sales.onboarding.domain.valueobject.*;
import com.gogidix.sales.onboarding.domain.model.OnboardingStep;
import com.gogidix.sales.onboarding.domain.model.DocumentChecklist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Onboarding Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnboardingResponseDto {

    private String id;
    private String onboardingId;
    private String tenantId;
    private String customerId;
    private String customerName;
    private String customerEmail;
    private CustomerTypeDto customerType;
    private String templateId;
    private String templateName;
    private OnboardingStatusDto status;
    private PriorityDto priority;
    private String assignedTo;
    private String assignedBy;
    private Instant assignedAt;
    private Instant startedAt;
    private Instant completedAt;
    private Instant estimatedCompletionDate;
    private Integer estimatedDurationHours;
    private Long actualDurationMinutes;
    private String initiatedBy;
    private String completedBy;
    private String notes;
    private String cancellationReason;
    private List<OnboardingStepDto> steps;
    private DocumentChecklistDto documentChecklist;
    private Map<String, Object> metadata;
    private Integer progressPercentage;
    private Instant lastProgressUpdate;
    private Instant createdAt;
    private Instant updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OnboardingStepDto {
        private String stepId;
        private String onboardingId;
        private String name;
        private String description;
        private StepTypeDto stepType;
        private StepStatusDto status;
        private Integer order;
        private Boolean optional;
        private Boolean autoComplete;
        private String assignedTo;
        private String assignedBy;
        private Instant startedAt;
        private Instant completedAt;
        private Long durationMinutes;
        private String completedBy;
        private String updatedBy;
        private Instant updatedAt;
        private List<String> dependencies;
        private String notes;
        private Integer estimatedDurationMinutes;
        private String helpUrl;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DocumentChecklistDto {
        private String id;
        private String checklistId;
        private String onboardingId;
        private String tenantId;
        private String customerId;
        private List<DocumentItemDto> documents;
        private Boolean requireAllDocuments;
        private Integer totalRequired;
        private Integer totalCompleted;
        private Boolean completed;
        private Instant completedAt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DocumentItemDto {
        private String itemId;
        private String documentType;
        private String fileName;
        private String fileUrl;
        private Long fileSizeBytes;
        private Boolean required;
        private String status;
        private String uploadedBy;
        private Instant uploadedAt;
        private String verifiedBy;
        private Instant verifiedAt;
        private String verificationNotes;
        private String notes;
    }

    public enum CustomerTypeDto {
        INDIVIDUAL, SMALL_BUSINESS, ENTERPRISE, PARTNER, RESELLER
    }

    public enum OnboardingStatusDto {
        NOT_STARTED, IN_PROGRESS, PENDING_REVIEW, COMPLETED, ON_HOLD, CANCELLED
    }

    public enum PriorityDto {
        LOW, MEDIUM, HIGH, URGENT
    }

    public enum StepTypeDto {
        ACCOUNT_SETUP, DOCUMENT_COLLECTION, VERIFICATION, CONTRACT_SIGNING,
        TRAINING, INTEGRATION, PAYMENT_SETUP, WELCOME_CALL, REVIEW, CUSTOM
    }

    public enum StepStatusDto {
        PENDING, IN_PROGRESS, COMPLETED, SKIPPED, FAILED
    }
}
