package com.gogidix.aiservices.aigatewayservice.domain.model;

import java.util.Map;
import java.util.Objects;

/**
 * Represents a filter applied to a gateway route.
 */
public class RouteFilter {

    private final String name;
    private final String type;
    private final Map<String, Object> parameters;

    public RouteFilter(String name, String type, Map<String, Object> parameters) {
        this.name = Objects.requireNonNull(name, "name is required");
        this.type = Objects.requireNonNull(type, "type is required");
        this.parameters = parameters;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RouteFilter that = (RouteFilter) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }

    @Override
    public String toString() {
        return "RouteFilter{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", parameters=" + parameters +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private String type;
        private Map<String, Object> parameters;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder parameters(Map<String, Object> parameters) {
            this.parameters = parameters;
            return this;
        }

        public RouteFilter build() {
            return new RouteFilter(name, type, parameters);
        }
    }
}
