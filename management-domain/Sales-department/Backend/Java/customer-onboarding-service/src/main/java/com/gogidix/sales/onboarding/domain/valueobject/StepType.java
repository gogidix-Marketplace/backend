package com.gogidix.sales.onboarding.domain.valueobject;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Step Type Value Object
 * Defines the type of onboarding step
 */
public enum StepType {
    ACCOUNT_SETUP("Account Setup", "Initial account configuration"),
    DOCUMENT_COLLECTION("Document Collection", "Collect required documents"),
    VERIFICATION("Verification", "Verify customer information"),
    CONTRACT_SIGNING("Contract Signing", "Sign contracts and agreements"),
    TRAINING("Training", "Product training session"),
    INTEGRATION("Integration", "System integration setup"),
    PAYMENT_SETUP("Payment Setup", "Configure payment methods"),
    WELCOME_CALL("Welcome Call", "Initial welcome call"),
    REVIEW("Review", "Review and approve onboarding"),
    CUSTOM("Custom", "Custom step type");

    private final String value;
    private final String description;

    StepType(String value, String description) {
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
    public static StepType fromValue(String value) {
        for (StepType type : StepType.values()) {
            if (type.value.equalsIgnoreCase(value) || type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown StepType: " + value);
    }
}
