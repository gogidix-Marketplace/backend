package com.gogidix.aiservices.aiproductrecommendationservice.domain.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Value object representing a unique Recommendation Result identifier.
 */
public class RecommendationResultId {

    private final UUID value;

    public RecommendationResultId() {
        this.value = UUID.randomUUID();
    }

    public RecommendationResultId(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException("RecommendationResultId value cannot be null");
        }
        this.value = value;
    }

    public static RecommendationResultId randomUUID() {
        return new RecommendationResultId(UUID.randomUUID());
    }

    public static RecommendationResultId of(String uuid) {
        return new RecommendationResultId(UUID.fromString(uuid));
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
        RecommendationResultId that = (RecommendationResultId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
