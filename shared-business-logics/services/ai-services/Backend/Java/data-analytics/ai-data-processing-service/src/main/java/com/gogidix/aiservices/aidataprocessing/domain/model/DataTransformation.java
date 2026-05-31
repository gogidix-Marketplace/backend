package com.gogidix.aiservices.aidataprocessing.domain.model;

import java.util.Map;

public class DataTransformation {
    private final String transformationId;
    private final TransformationType type;
    private final Map<String, Object> config;
    private final int order;

    private DataTransformation(Builder builder) {
        this.transformationId = builder.transformationId;
        this.type = builder.type;
        this.config = builder.config != null ? builder.config : Map.of();
        this.order = builder.order;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getTransformationId() { return transformationId; }
    public TransformationType getType() { return type; }
    public Map<String, Object> getConfig() { return config; }
    public int getOrder() { return order; }

    public static class Builder {
        private String transformationId;
        private TransformationType type;
        private Map<String, Object> config;
        private int order;

        public Builder transformationId(String transformationId) {
            this.transformationId = transformationId;
            return this;
        }

        public Builder type(TransformationType type) {
            this.type = type;
            return this;
        }

        public Builder config(Map<String, Object> config) {
            this.config = config;
            return this;
        }

        public Builder order(int order) {
            this.order = order;
            return this;
        }

        public DataTransformation build() {
            if (type == null) {
                throw new IllegalArgumentException("Transformation type cannot be null");
            }
            if (transformationId == null) {
                transformationId = java.util.UUID.randomUUID().toString();
            }
            return new DataTransformation(this);
        }
    }
}
