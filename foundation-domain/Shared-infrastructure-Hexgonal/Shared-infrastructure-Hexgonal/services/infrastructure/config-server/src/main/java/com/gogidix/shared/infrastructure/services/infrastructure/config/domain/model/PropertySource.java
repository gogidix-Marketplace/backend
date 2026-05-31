package com.gogidix.shared.infrastructure.services.infrastructure.config.domain.model;

import java.util.Map;
import java.util.Objects;

/**
 * Property Source domain model.
 * Represents a source of configuration properties.
 */
public class PropertySource {

    private final String name;
    private final Map<String, Object> source;

    private PropertySource(Builder builder) {
        this.name = builder.name;
        this.source = builder.source;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getName() {
        return name;
    }

    public Map<String, Object> getSource() {
        return source;
    }

    public Object getProperty(String key) {
        return source.get(key);
    }

    public boolean containsProperty(String key) {
        return source.containsKey(key);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PropertySource that = (PropertySource) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "PropertySource{" +
            "name='" + name + '\'' +
            ", sourceSize=" + (source != null ? source.size() : 0) +
            '}';
    }

    public static class Builder {
        private String name;
        private Map<String, Object> source;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder source(Map<String, Object> source) {
            this.source = source;
            return this;
        }

        public PropertySource build() {
            return new PropertySource(this);
        }
    }
}
