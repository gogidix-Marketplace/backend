package com.gogidix.sales.onboarding.domain.model;

import com.gogidix.sales.onboarding.domain.valueobject.CustomerType;
import com.gogidix.sales.onboarding.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Onboarding Template Domain Entity
 * Defines reusable onboarding templates for different customer types
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "onboarding_templates")
public class OnboardingTemplate extends BaseEntity {

    private String templateId;

    private String tenantId;

    private String name;

    private String description;

    private CustomerType customerType;

    private Boolean active;

    private Integer version;

    private String parentTemplateId;

    @Builder.Default
    private List<TemplateStep> steps = new ArrayList<>();

    @Builder.Default
    private List<String> requiredDocumentTypes = new ArrayList<>();

    private Integer estimatedDurationHours;

    private String welcomeEmailTemplate;

    private Boolean autoAssign;

    private String defaultAssigneeRole;

    private String createdBy;

    private String updatedBy;

    /**
     * Creates a new template
     */
    public static OnboardingTemplate create(String tenantId, String name, String description,
                                             CustomerType customerType, List<TemplateStep> steps,
                                             Integer estimatedDurationHours, String createdBy) {
        OnboardingTemplate template = OnboardingTemplate.builder()
                .templateId(generateTemplateId())
                .tenantId(tenantId)
                .name(name)
                .description(description)
                .customerType(customerType)
                .active(true)
                .version(1)
                .steps(steps != null ? steps : new ArrayList<>())
                .estimatedDurationHours(estimatedDurationHours != null ? estimatedDurationHours : 24)
                .createdBy(createdBy)
                .build();

        return template;
    }

    /**
     * Adds a step to the template
     */
    public void addStep(TemplateStep step) {
        if (this.steps == null) {
            this.steps = new ArrayList<>();
        }
        step.setOrder(this.steps.size() + 1);
        this.steps.add(step);
    }

    /**
     * Removes a step from the template
     */
    public void removeStep(String stepId) {
        if (this.steps != null) {
            this.steps.removeIf(s -> s.getStepId().equals(stepId));
            reorderSteps();
        }
    }

    /**
     * Reorders steps after removal
     */
    private void reorderSteps() {
        for (int i = 0; i < this.steps.size(); i++) {
            this.steps.get(i).setOrder(i + 1);
        }
    }

    /**
     * Activates the template
     */
    public void activate() {
        this.active = true;
    }

    /**
     * Deactivates the template
     */
    public void deactivate() {
        this.active = false;
    }

    /**
     * Creates a new version of the template
     */
    public OnboardingTemplate createNewVersion(String updatedBy) {
        OnboardingTemplate newVersion = OnboardingTemplate.builder()
                .templateId(generateTemplateId())
                .tenantId(this.tenantId)
                .name(this.name)
                .description(this.description)
                .customerType(this.customerType)
                .active(true)
                .version(this.version + 1)
                .steps(new ArrayList<>(this.steps))
                .requiredDocumentTypes(new ArrayList<>(this.requiredDocumentTypes))
                .estimatedDurationHours(this.estimatedDurationHours)
                .welcomeEmailTemplate(this.welcomeEmailTemplate)
                .autoAssign(this.autoAssign)
                .defaultAssigneeRole(this.defaultAssigneeRole)
                .parentTemplateId(this.templateId)
                .build();

        this.active = false;

        return newVersion;
    }

    /**
     * Converts template steps to onboarding steps
     */
    public List<OnboardingStep> createOnboardingSteps(String onboardingId) {
        List<OnboardingStep> steps = new ArrayList<>();
        if (this.steps != null) {
            for (TemplateStep templateStep : this.steps) {
                OnboardingStep step = OnboardingStep.builder()
                        .stepId(generateStepId())
                        .onboardingId(onboardingId)
                        .name(templateStep.getName())
                        .description(templateStep.getDescription())
                        .stepType(templateStep.getStepType())
                        .status(com.gogidix.sales.onboarding.domain.valueobject.StepStatus.PENDING)
                        .order(templateStep.getOrder())
                        .optional(templateStep.getOptional())
                        .autoComplete(templateStep.getAutoComplete())
                        .dependencies(templateStep.getDependencies())
                        .templateStepId(templateStep.getStepId())
                        .estimatedDurationMinutes(templateStep.getEstimatedDurationMinutes())
                        .helpUrl(templateStep.getHelpUrl())
                        .requiredFields(templateStep.getRequiredFields())
                        .build();
                steps.add(step);
            }
        }
        return steps;
    }

    private static String generateTemplateId() {
        return "TPL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private static String generateStepId() {
        return "STP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
