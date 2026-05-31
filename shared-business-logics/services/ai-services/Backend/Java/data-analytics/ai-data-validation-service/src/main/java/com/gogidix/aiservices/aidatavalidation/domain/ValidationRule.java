package com.gogidix.aiservices.aidatavalidation.domain;

import lombok.Value;
import lombok.Builder;
import lombok.NonNull;

import java.util.Map;

/**
 * Value object representing a validation rule.
 */
@Value
@Builder
public class ValidationRule implements Comparable<ValidationRule> {

    @NonNull
    String ruleId;

    @NonNull
    String ruleName;

    @NonNull
    ValidationRuleType type;

    @NonNull
    String fieldPath;

    @NonNull
    Map<String, Object> parameters;

    @Builder.Default
    int priority = 5;

    public enum ValidationRuleType {
        REQUIRED,
        FORMAT,
        RANGE,
        LENGTH,
        PATTERN,
        CUSTOM,
        SCHEMA
    }

    @Override
    public int compareTo(ValidationRule other) {
        return Integer.compare(this.priority, other.priority);
    }

    public boolean isNewerThan(ValidationRule other) {
        return this.priority < other.priority;
    }
}
