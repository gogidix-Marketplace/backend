package com.gogidix.aiservices.aidataprocessing.domain.model;

import java.util.Map;

public class ValidationRule {
    private final String ruleId;
    private final String field;
    private final ValidationType type;
    private final Map<String, Object> config;
    private final boolean required;

    private ValidationRule(Builder builder) {
        this.ruleId = builder.ruleId;
        this.field = builder.field;
        this.type = builder.type;
        this.config = builder.config != null ? builder.config : Map.of();
        this.required = builder.required;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getRuleId() { return ruleId; }
    public String getField() { return field; }
    public ValidationType getType() { return type; }
    public Map<String, Object> getConfig() { return config; }
    public boolean isRequired() { return required; }

    public static class Builder {
        private String ruleId;
        private String field;
        private ValidationType type;
        private Map<String, Object> config;
        private boolean required = true;

        public Builder ruleId(String ruleId) {
            this.ruleId = ruleId;
            return this;
        }

        public Builder field(String field) {
            this.field = field;
            return this;
        }

        public Builder type(ValidationType type) {
            this.type = type;
            return this;
        }

        public Builder config(Map<String, Object> config) {
            this.config = config;
            return this;
        }

        public Builder required(boolean required) {
            this.required = required;
            return this;
        }

        public ValidationRule build() {
            if (type == null) {
                throw new IllegalArgumentException("Validation type cannot be null");
            }
            if (field == null || field.trim().isEmpty()) {
                throw new IllegalArgumentException("Field cannot be null or empty");
            }
            if (ruleId == null) {
                ruleId = java.util.UUID.randomUUID().toString();
            }
            return new ValidationRule(this);
        }
    }
}
