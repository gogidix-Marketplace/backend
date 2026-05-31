package com.gogidix.aiservices.aidatavalidation.infrastructure.persistence;

import com.gogidix.aiservices.aidatavalidation.domain.model.Severity;
import com.gogidix.aiservices.aidatavalidation.domain.model.ValidationType;
import lombok.Data;

import java.util.Map;

@Data
public class ValidationRuleEntity {
    private String ruleId;
    private String name;
    private ValidationType type;
    private Map<String, Object> configuration;
    private Severity severity;
    private int priority;
    private boolean enabled;
}
