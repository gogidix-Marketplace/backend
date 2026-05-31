package com.gogidix.sales.onboarding.domain.model;

import com.gogidix.sales.onboarding.domain.valueobject.StepType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Template Step Entity
 * Defines a step within an onboarding template
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TemplateStep {

    private String stepId;

    private String name;

    private String description;

    private StepType stepType;

    private Integer order;

    @Builder.Default
    private Boolean optional = false;

    @Builder.Default
    private Boolean autoComplete = false;

    private List<String> dependencies;

    private Integer estimatedDurationMinutes;

    private String helpUrl;

    private List<String> requiredFields;

    private String assigneeRole;
}
