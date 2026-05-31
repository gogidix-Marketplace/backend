package com.gogidix.aiservices.aidatavalidation.domain.model;

import java.util.Map;

public class ValidationRule {
    private static final int MIN_PRIORITY = 1;
    private static final int MAX_PRIORITY = 10;

    private final String ruleId;
    private final String name;
    private final ValidationType type;
    private Map<String, Object> configuration;
    private Severity severity;
    private int priority;
    private boolean enabled;

    private ValidationRule(String ruleId, String name, ValidationType type) {
        if (ruleId == null) {
            throw new IllegalArgumentException("Rule ID cannot be null");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Rule name cannot be null or empty");
        }
        if (type == null) {
            throw new IllegalArgumentException("Validation type cannot be null");
        }

        this.ruleId = ruleId;
        this.name = name;
        this.type = type;
        this.configuration = Map.of();
        this.severity = Severity.MEDIUM;
        this.priority = 5;
        this.enabled = true;
    }

    public static ValidationRule create(String ruleId, String name, ValidationType type) {
        return new ValidationRule(ruleId, name, type);
    }

    public static ValidationRule restore(String ruleId, String name, ValidationType type,
                                        Map<String, Object> configuration, Severity severity,
                                        int priority, boolean enabled) {
        ValidationRule rule = new ValidationRule(ruleId, name, type);
        rule.configuration = configuration;
        rule.severity = severity;
        rule.priority = priority;
        rule.enabled = enabled;
        return rule;
    }

    public void setConfiguration(Map<String, Object> configuration) {
        if (configuration == null) {
            throw new IllegalArgumentException("Configuration cannot be null");
        }
        this.configuration = configuration;
    }

    public void setSeverity(Severity severity) {
        if (severity == null) {
            throw new IllegalArgumentException("Severity cannot be null");
        }
        this.severity = severity;
    }

    public void setPriority(int priority) {
        if (priority < MIN_PRIORITY || priority > MAX_PRIORITY) {
            throw new IllegalArgumentException("Priority must be between " + MIN_PRIORITY + " and " + MAX_PRIORITY);
        }
        this.priority = priority;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getRuleId() { return ruleId; }
    public String getName() { return name; }
    public ValidationType getType() { return type; }
    public Map<String, Object> getConfiguration() { return configuration; }
    public Severity getSeverity() { return severity; }
    public int getPriority() { return priority; }
    public boolean isEnabled() { return enabled; }
}
