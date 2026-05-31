package com.gogidix.hr.globalcompliance.domain.port.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Check Commands (Input Port)
 * Defines the input commands for compliance check operations
 */
public interface CheckCommand {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CreateCheckCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Requirement ID is required")
        private String requirementId;

        @NotBlank(message = "Requirement name is required")
        private String requirementName;

        @NotBlank(message = "Country code is required")
        private String countryCode;

        @NotNull(message = "Scheduled date is required")
        private LocalDate scheduledDate;

        private String frequency;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class StartCheckCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Check ID is required")
        private String checkId;

        @NotBlank(message = "Checked by is required")
        private String checkedBy;

        private String checkedByName;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CompleteCheckCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Check ID is required")
        private String checkId;

        @NotBlank(message = "Result is required")
        private String result;

        private String findings;

        private String correctiveAction;

        private LocalDate targetCompletionDate;

        private List<String> supportingDocuments;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class PassCheckCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Check ID is required")
        private String checkId;

        private String findings;

        private List<String> supportingDocuments;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class FailCheckCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Check ID is required")
        private String checkId;

        private String findings;

        private String correctiveAction;

        private LocalDate targetCompletionDate;

        private List<String> supportingDocuments;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class WaiveCheckCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Check ID is required")
        private String checkId;

        @NotBlank(message = "Reason is required")
        private String reason;

        @NotBlank(message = "Waived by is required")
        private String waivedBy;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class MarkAsNotApplicableCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Check ID is required")
        private String checkId;

        @NotBlank(message = "Reason is required")
        private String reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class RescheduleCheckCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Check ID is required")
        private String checkId;

        @NotNull(message = "New scheduled date is required")
        private LocalDate newScheduledDate;

        private String reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class UpdateCorrectiveActionCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Check ID is required")
        private String checkId;

        private String correctiveAction;

        private LocalDate actualCompletionDate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class AddCommentCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Check ID is required")
        private String checkId;

        @NotBlank(message = "Comment is required")
        private String comment;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class DeleteCheckCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Check ID is required")
        private String checkId;
    }
}
