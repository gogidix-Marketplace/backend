package com.gogidix.sales.onboarding.domain.valueobject;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Customer Type Value Object
 * Defines the type of customer being onboarded
 */
public enum CustomerType {
    INDIVIDUAL("Individual", "Individual customer"),
    SMALL_BUSINESS("Small Business", "Small business with 1-50 employees"),
    ENTERPRISE("Enterprise", "Large enterprise with 50+ employees"),
    PARTNER("Partner", "Business partner"),
    RESELLER("Reseller", "Reseller partner");

    private final String value;
    private final String description;

    CustomerType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static CustomerType fromValue(String value) {
        for (CustomerType type : CustomerType.values()) {
            if (type.value.equalsIgnoreCase(value) || type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown CustomerType: " + value);
    }
}
