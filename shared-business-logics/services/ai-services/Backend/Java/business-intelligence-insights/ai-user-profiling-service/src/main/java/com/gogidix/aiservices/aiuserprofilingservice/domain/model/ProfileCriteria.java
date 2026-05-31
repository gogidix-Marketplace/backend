package com.gogidix.aiservices.aiuserprofilingservice.domain.model;

import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Value Object representing segment criteria.
 * Immutable by design.
 */
public class ProfileCriteria {

    @Field("criteria_type")
    private final CriteriaType type;

    @Field("operator")
    private final CriteriaOperator operator;

    @Field("field")
    private final String field;

    @Field("value")
    private final Object value;

    @Field("logical_operator")
    private final LogicalOperator logicalOperator;

    @Field("nested_criteria")
    private final List<ProfileCriteria> nestedCriteria;

    // Private constructor
    private ProfileCriteria(Builder builder) {
        this.type = Objects.requireNonNull(builder.type, "type is required");
        this.operator = Objects.requireNonNull(builder.operator, "operator is required");
        this.field = Objects.requireNonNull(builder.field, "field is required");
        this.value = builder.value;
        this.logicalOperator = builder.logicalOperator != null ? builder.logicalOperator : LogicalOperator.AND;
        this.nestedCriteria = builder.nestedCriteria != null ? List.copyOf(builder.nestedCriteria) : Collections.emptyList();
    }

    public static Builder builder() {
        return new Builder();
    }

    // Getters
    public CriteriaType getType() {
        return type;
    }

    public CriteriaOperator getOperator() {
        return operator;
    }

    public String getField() {
        return field;
    }

    public Object getValue() {
        return value;
    }

    public LogicalOperator getLogicalOperator() {
        return logicalOperator;
    }

    public List<ProfileCriteria> getNestedCriteria() {
        return Collections.unmodifiableList(nestedCriteria);
    }

    /**
     * Check if this criteria has nested criteria.
     *
     * @return true if has nested criteria, false otherwise
     */
    public boolean hasNestedCriteria() {
        return nestedCriteria != null && !nestedCriteria.isEmpty();
    }

    /**
     * Add a nested criteria.
     *
     * @param criteria the criteria to add
     * @return a new ProfileCriteria with the nested criteria added
     */
    public ProfileCriteria withNestedCriteria(ProfileCriteria criteria) {
        List<ProfileCriteria> newNested = new ArrayList<>(this.nestedCriteria);
        newNested.add(criteria);
        return builder()
                .type(this.type)
                .operator(this.operator)
                .field(this.field)
                .value(this.value)
                .logicalOperator(this.logicalOperator)
                .nestedCriteria(newNested)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProfileCriteria that = (ProfileCriteria) o;
        return type == that.type &&
                operator == that.operator &&
                Objects.equals(field, that.field) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, operator, field, value);
    }

    @Override
    public String toString() {
        return "ProfileCriteria{" +
                "type=" + type +
                ", operator=" + operator +
                ", field='" + field + '\'' +
                ", value=" + value +
                ", logicalOperator=" + logicalOperator +
                ", nestedCriteriaCount=" + (nestedCriteria != null ? nestedCriteria.size() : 0) +
                '}';
    }

    /**
     * Builder for ProfileCriteria.
     */
    public static class Builder {
        private CriteriaType type;
        private CriteriaOperator operator;
        private String field;
        private Object value;
        private LogicalOperator logicalOperator;
        private List<ProfileCriteria> nestedCriteria;

        public Builder type(CriteriaType type) {
            this.type = type;
            return this;
        }

        public Builder operator(CriteriaOperator operator) {
            this.operator = operator;
            return this;
        }

        public Builder field(String field) {
            this.field = field;
            return this;
        }

        public Builder value(Object value) {
            this.value = value;
            return this;
        }

        public Builder value(String value) {
            this.value = value;
            return this;
        }

        public Builder value(Number value) {
            this.value = value;
            return this;
        }

        public Builder value(Boolean value) {
            this.value = value;
            return this;
        }

        public Builder value(List<?> value) {
            this.value = value;
            return this;
        }

        public Builder logicalOperator(LogicalOperator logicalOperator) {
            this.logicalOperator = logicalOperator;
            return this;
        }

        public Builder nestedCriteria(List<ProfileCriteria> nestedCriteria) {
            this.nestedCriteria = nestedCriteria;
            return this;
        }

        public Builder addNestedCriteria(ProfileCriteria criteria) {
            if (this.nestedCriteria == null) {
                this.nestedCriteria = new ArrayList<>();
            }
            this.nestedCriteria.add(criteria);
            return this;
        }

        public ProfileCriteria build() {
            return new ProfileCriteria(this);
        }
    }

    /**
     * Criteria type enum.
     */
    public enum CriteriaType {
        BEHAVIOR,
        DEMOGRAPHIC,
        TRANSACTION,
        CUSTOM
    }

    /**
     * Criteria operator enum.
     */
    public enum CriteriaOperator {
        EQUALS,
        NOT_EQUALS,
        CONTAINS,
        NOT_CONTAINS,
        STARTS_WITH,
        ENDS_WITH,
        GREATER_THAN,
        LESS_THAN,
        GREATER_THAN_OR_EQUAL,
        LESS_THAN_OR_EQUAL,
        BETWEEN,
        IN,
        NOT_IN,
        IS_NULL,
        IS_NOT_NULL,
        REGEX
    }

    /**
     * Logical operator for combining criteria.
     */
    public enum LogicalOperator {
        AND,
        OR
    }
}
