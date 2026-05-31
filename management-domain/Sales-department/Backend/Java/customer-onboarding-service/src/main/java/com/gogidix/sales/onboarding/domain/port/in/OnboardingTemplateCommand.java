package com.gogidix.sales.onboarding.domain.port.in;

import com.gogidix.sales.onboarding.domain.model.TemplateStep;
import com.gogidix.sales.onboarding.domain.valueobject.CustomerType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Onboarding Template Commands (Input Port)
 * Defines the input commands for onboarding template operations
 */
public interface OnboardingTemplateCommand {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class CreateTemplateCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Name is required")
        private String name;

        private String description;

        @NotNull(message = "Customer type is required")
        private CustomerType customerType;

        private List<TemplateStep> steps;

        private List<String> requiredDocumentTypes;

        private Integer estimatedDurationHours;

        private String welcomeEmailTemplate;

        private Boolean autoAssign;

        private String defaultAssigneeRole;

        @NotBlank(message = "Created by is required")
        private String createdBy;
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

        private List<TemplateStep> steps;

        private List<String> requiredDocumentTypes;

        private Integer estimatedDurationHours;

        private String welcomeEmailTemplate;

        private Boolean autoAssign;

        private String defaultAssigneeRole;

        @NotBlank(message = "Updated by is required")
        private String updatedBy;
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
    class CreateNewVersionCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Template ID is required")
        private String templateId;

        @NotBlank(message = "Updated by is required")
        private String updatedBy;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class AddStepCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Template ID is required")
        private String templateId;

        @NotNull(message = "Step is required")
        private TemplateStep step;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class RemoveStepCommand {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Template ID is required")
        private String templateId;

        @NotBlank(message = "Step ID is required")
        private String stepId;
    }
}
