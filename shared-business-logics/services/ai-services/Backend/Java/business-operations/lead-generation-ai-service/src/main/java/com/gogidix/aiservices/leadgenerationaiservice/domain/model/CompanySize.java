package com.gogidix.aiservices.leadgenerationaiservice.domain.model;

import lombok.Getter;

@Getter
public enum CompanySize {
    SOLO("1", "Solo"),
    MICRO("1-10", "Micro"),
    SMALL_BUSINESS("11-50", "Small Business"),
    MEDIUM_BUSINESS("51-200", "Medium Business"),
    LARGE("201-1000", "Large"),
    ENTERPRISE("1000+", "Enterprise"),
    NOT_SPECIFIED("", "Not Specified");

    private final String employeeRange;
    private final String displayName;

    CompanySize(String employeeRange, String displayName) {
        this.employeeRange = employeeRange;
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    public int getMinEmployees() {
        return switch (this) {
            case SOLO -> 1;
            case MICRO -> 1;
            case SMALL_BUSINESS -> 11;
            case MEDIUM_BUSINESS -> 51;
            case LARGE -> 201;
            case ENTERPRISE -> 1000;
            default -> 0;
        };
    }

    public int getMaxEmployees() {
        return switch (this) {
            case SOLO -> 1;
            case MICRO -> 10;
            case SMALL_BUSINESS -> 50;
            case MEDIUM_BUSINESS -> 200;
            case LARGE -> 1000;
            case ENTERPRISE -> Integer.MAX_VALUE;
            default -> 0;
        };
    }
}
