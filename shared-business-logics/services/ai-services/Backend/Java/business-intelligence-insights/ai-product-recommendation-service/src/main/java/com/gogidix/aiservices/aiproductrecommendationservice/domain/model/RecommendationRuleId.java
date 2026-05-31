package com.gogidix.aiservices.aiproductrecommendationservice.domain.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Value object representing a unique Recommendation Rule identifier.
 */
public class RecommendationRuleId {

    private final UUID value;

    public RecommendationRuleId() {
        this.value = UUID.randomUUID();
    }

    public RecommendationRuleId(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException("RecommendationRuleId value cannot be null");
        }
        this.value = value;
    }

    public static RecommendationRuleId randomUUID() {
        return new RecommendationRuleId(UUID.randomUUID());
    }

    public static RecommendationRuleId fromString(String uuid) {
        try {
            return new RecommendationRuleId(UUID.fromString(uuid));
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid UUID string: " + uuid, e);
        }
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecommendationRuleId that = (RecommendationRuleId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
