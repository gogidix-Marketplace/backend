package com.gogidix.transaction.onboarding.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "onboarding_trackers")
@CompoundIndex(name = "idx_entity", def = "{'entityType': 1, 'entityId': 1}")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnboardingTracker {

    @Id
    @Field("id")
    @Indexed
    @Builder.Default
    private UUID id = UUID.randomUUID();

    @Field("transaction_id")
    @Indexed(unique = true)
    private UUID transactionId;

    @Field("entity_type")
    @Indexed
    private EntityType entityType;

    @Field("entity_id")
    private String entityId;

    @Field("merchant_id")
    @Indexed
    private String merchantId;

    @Field("current_status")
    @Indexed
    @Builder.Default
    private OnboardingStatus currentStatus = OnboardingStatus.INITIATED;

    @Field("current_stage")
    @Indexed
    @Builder.Default
    private OnboardingStage currentStage = OnboardingStage.REGISTRATION;

    @Field("previous_stage")
    private OnboardingStage previousStage;

    @Field("stage_description")
    private String stageDescription;

    @Field("completed_stages")
    private String completedStages;

    @Field("failed_stages")
    private String failedStages;

    @Field("progress_percentage")
    @Builder.Default
    private Integer progressPercentage = 0;

    @Field("total_steps")
    private Integer totalSteps;

    @Field("completed_steps")
    @Builder.Default
    private Integer completedSteps = 0;

    @Field("onboarding_data")
    private String onboardingData;

    @Field("verification_documents")
    private String verificationDocuments;

    @Field("priority")
    @Builder.Default
    private Priority priority = Priority.NORMAL;

    @Field("estimated_completion_hours")
    private Integer estimatedCompletionHours;

    @Field("actual_completion_hours")
    private Integer actualCompletionHours;

    @Field("assigned_to")
    private String assignedTo;

    @Field("error_message")
    private String errorMessage;

    @Field("retry_count")
    @Builder.Default
    private Integer retryCount = 0;

    @Field("max_retries")
    @Builder.Default
    private Integer maxRetries = 3;

    @Field("metadata")
    private String metadata;

    @Field("idempotency_key")
    @Indexed(unique = true)
    private String idempotencyKey;

    @Field("initiated_by")
    private String initiatedBy;

    @Field("initiated_at")
    private LocalDateTime initiatedAt;

    @Field("completed_at")
    private LocalDateTime completedAt;

    @Field("last_state_change")
    private LocalDateTime lastStateChange;

    @Field("created_at")
    @CreatedDate
    @Indexed
    private LocalDateTime createdAt;

    @Field("updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public enum EntityType {
        MERCHANT,
        USER,
        BUSINESS,
        VENDOR,
        PARTNER
    }

    public enum OnboardingStatus {
        INITIATED,
        IN_PROGRESS,
        PENDING_VERIFICATION,
        UNDER_REVIEW,
        APPROVED,
        REJECTED,
        COMPLETED,
        FAILED,
        CANCELLED,
        ON_HOLD
    }

    public enum OnboardingStage {
        REGISTRATION,
        KYC_VERIFICATION,
        BUSINESS_VERIFICATION,
        DOCUMENT_UPLOAD,
        DOCUMENT_VERIFICATION,
        BANK_ACCOUNT_SETUP,
        COMPLIANCE_CHECK,
        RISK_ASSESSMENT,
        APPROVAL,
        ACTIVATION,
        COMPLETED
    }

    public enum Priority {
        LOW,
        NORMAL,
        HIGH,
        URGENT
    }
}
