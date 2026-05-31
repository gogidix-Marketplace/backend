package com.gogidix.dashboard.reporting.domain.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Report ID Value Object
 */
public class ReportId {
    private final String value;
    
    private ReportId(String value) {
        this.value = Objects.requireNonNull(value, "Report ID cannot be null");
        if (value.trim().isEmpty()) {
            throw new IllegalArgumentException("Report ID cannot be empty");
        }
    }
    
    public static ReportId generate() {
        return new ReportId(UUID.randomUUID().toString());
    }
    
    public static ReportId of(String value) {
        return new ReportId(value);
    }

    public static ReportId fromString(String value) {
        return of(value);
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReportId reportId = (ReportId) o;
        return Objects.equals(value, reportId.value);
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