package com.gogidix.aiservices.leadgenerationaiservice.domain.model;

import lombok.Getter;

@Getter
public enum LeadStatus {
    NEW("new"),
    CONTACTED("contacted"),
    QUALIFIED("qualified"),
    PROPOSAL_SENT("proposal_sent"),
    NEGOTIATION("negotiation"),
    CONVERTED("converted"),
    LOST("lost");

    private final String value;

    LeadStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static LeadStatus fromString(String value) {
        for (LeadStatus status : LeadStatus.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown lead status: " + value);
    }

    public boolean isClosed() {
        return this == CONVERTED || this == LOST;
    }

    public boolean isActive() {
        return !isClosed();
    }
}
