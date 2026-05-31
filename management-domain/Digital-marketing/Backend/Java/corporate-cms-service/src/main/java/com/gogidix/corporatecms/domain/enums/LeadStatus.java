package com.gogidix.corporatecms.domain.enums;

import lombok.Getter;

/**
 * Enumeration representing lead status.
 */
@Getter
public enum LeadStatus {
    NEW("new", "New lead"),
    CONTACTED("contacted", "Lead has been contacted"),
    QUALIFIED("qualified", "Lead has been qualified"),
    PROPOSAL("proposal", "Proposal sent"),
    NEGOTIATION("negotiation", "In negotiation"),
    WON("won", "Lead converted to customer"),
    LOST("lost", "Lead lost"),
    UNQUALIFIED("unqualified", "Lead not qualified");

    private final String code;
    private final String description;

    LeadStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static LeadStatus fromCode(String code) {
        for (LeadStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown lead status: " + code);
    }
}
