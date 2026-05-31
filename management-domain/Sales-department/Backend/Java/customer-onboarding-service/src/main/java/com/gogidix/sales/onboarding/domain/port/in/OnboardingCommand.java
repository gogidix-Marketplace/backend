package com.gogidix.sales.onboarding.domain.port.in;

import com.gogidix.sales.onboarding.domain.model.OnboardingStep;
import com.gogidix.sales.onboarding.domain.valueobject.CustomerType;
import com.gogidix.sales.onboarding.domain.valueobject.Priority;
import com.gogidix.sales.onboarding.domain.valueobject.StepStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Onboarding Commands (Input Port)
 * Defines the input commands for onboarding operations
 */
public interface OnboardingCommand {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CreateOnboardingCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Customer ID is required")
        private String customerId;

        @NotBlank(message = "Customer name is required")
        private String customerName;

        @NotBlank(message = "Customer email is required")
        private String customerEmail;

        @NotNull(message = "Customer type is required")
        private CustomerType customerType;

        @NotBlank(message = "Template ID is required")
        private String templateId;

        private String templateName;

        @NotBlank(message = "Initiated by is required")
        private String initiatedBy;

        private Priority priority;

        private List<OnboardingStep> steps;

        private Integer estimatedDurationHours;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class StartOnboardingCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Onboarding ID is required")
        private String onboardingId;

        @NotBlank(message = "Started by is required")
        private String startedBy;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateStepCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Onboarding ID is required")
        private String onboardingId;

        @NotBlank(message = "Step ID is required")
        private String stepId;

        @NotNull(message = "Step status is required")
        private StepStatus status;

        @NotBlank(message = "Updated by is required")
        private String updatedBy;

        private String notes;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class SkipStepCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Onboarding ID is required")
        private String onboardingId;

        @NotBlank(message = "Step ID is required")
        private String stepId;

        @NotBlank(message = "Skipped by is required")
        private String skippedBy;

        private String reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class AssignOnboardingCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Onboarding ID is required")
        private String onboardingId;

        @NotBlank(message = "Assignee is required")
        private String assignee;

        @NotBlank(message = "Assigned by is required")
        private String assignedBy;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CompleteOnboardingCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Onboarding ID is required")
        private String onboardingId;

        @NotBlank(message = "Completed by is required")
        private String completedBy;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class PutOnHoldCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Onboarding ID is required")
        private String onboardingId;

        @NotBlank(message = "Reason is required")
        private String reason;

        @NotBlank(message = "Updated by is required")
        private String updatedBy;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ResumeOnboardingCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Onboarding ID is required")
        private String onboardingId;

        @NotBlank(message = "Resumed by is required")
        private String resumedBy;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CancelOnboardingCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Onboarding ID is required")
        private String onboardingId;

        @NotBlank(message = "Reason is required")
        private String reason;

        @NotBlank(message = "Cancelled by is required")
        private String cancelledBy;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UploadDocumentCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Onboarding ID is required")
        private String onboardingId;

        @NotBlank(message = "Checklist ID is required")
        private String checklistId;

        @NotBlank(message = "Item ID is required")
        private String itemId;

        @NotBlank(message = "File name is required")
        private String fileName;

        @NotBlank(message = "File URL is required")
        private String fileUrl;

        private Long fileSizeBytes;

        @NotBlank(message = "Uploaded by is required")
        private String uploadedBy;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class VerifyDocumentCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Onboarding ID is required")
        private String onboardingId;

        @NotBlank(message = "Checklist ID is required")
        private String checklistId;

        @NotBlank(message = "Item ID is required")
        private String itemId;

        @NotNull(message = "Approved is required")
        private Boolean approved;

        @NotBlank(message = "Verified by is required")
        private String verifiedBy;

        private String notes;
    }
}
