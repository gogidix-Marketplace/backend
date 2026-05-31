package com.gogidix.aiservices.leadgenerationaiservice.domain.model;

import lombok.Getter;

@Getter
public enum LeadLossReason {
    NOT_INTERESTED("not_interested", "Not interested in our solution"),
    BUDGET_CONSTRAINTS("budget", "Budget constraints"),
    WENT_WITH_COMPETITOR("competitor", "Went with a competitor"),
    TIMING("timing", "Bad timing"),
    NO_DECISION_MAKER_ACCESS("no_decision_maker", "Cannot reach decision maker"),
    TECHNICAL_MISMATCH("technical", "Technical mismatch"),
    COMPANY_CLOSED("company_closed", "Company went out of business"),
    CONTACT_UNRESPONSIVE("unresponsive", "Contact became unresponsive"),
    PRICING("pricing", "Pricing was too high"),
    FOUND_ALTERNATIVE("alternative", "Found an alternative solution"),
    OTHER("other", "Other reason");

    private final String value;
    private final String description;

    LeadLossReason(String value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public String toString() {
        return value;
    }

    public static LeadLossReason fromString(String value) {
        for (LeadLossReason reason : LeadLossReason.values()) {
            if (reason.value.equalsIgnoreCase(value)) {
                return reason;
            }
        }
        throw new IllegalArgumentException("Unknown loss reason: " + value);
    }
}
