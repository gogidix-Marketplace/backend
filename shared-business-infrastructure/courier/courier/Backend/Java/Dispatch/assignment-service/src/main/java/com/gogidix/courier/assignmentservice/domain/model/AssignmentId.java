package com.gogidix.courier.assignmentservice.domain.model;

import java.util.Objects;
import java.util.UUID;

public final class AssignmentId {

    private final String value;

    public AssignmentId() {
        this.value = UUID.randomUUID().toString();
    }

    public AssignmentId(String value) {
        this.value = Objects.requireNonNull(value, "AssignmentId value must not be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException("AssignmentId value must not be blank");
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssignmentId that = (AssignmentId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
