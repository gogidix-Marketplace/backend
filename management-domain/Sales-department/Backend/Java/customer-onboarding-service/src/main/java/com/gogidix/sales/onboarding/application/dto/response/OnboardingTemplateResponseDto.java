package com.gogidix.sales.onboarding.application.dto.response;

import com.gogidix.sales.onboarding.domain.model.TemplateStep;
import com.gogidix.sales.onboarding.domain.valueobject.CustomerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

/**
 * Onboarding Template Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnboardingTemplateResponseDto {

    private String id;
    private String templateId;
    private String tenantId;
    private String name;
    private String description;
    private CustomerType customerType;
    private Boolean active;
    private Integer version;
    private String parentTemplateId;
    private List<TemplateStepDto> steps;
    private List<String> requiredDocumentTypes;
    private Integer estimatedDurationHours;
    private String welcomeEmailTemplate;
    private Boolean autoAssign;
    private String defaultAssigneeRole;
    private Instant createdAt;
    private Instant updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TemplateStepDto {
        private String stepId;
        private String name;
        private String description;
        private String stepType;
        private Integer order;
        private Boolean optional;
        private Boolean autoComplete;
        private List<String> dependencies;
        private Integer estimatedDurationMinutes;
        private String helpUrl;
        private List<String> requiredFields;
        private String assigneeRole;
    }
}
