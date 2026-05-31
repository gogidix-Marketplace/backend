package com.gogidix.corporatecms.domain.enums;

import lombok.Getter;

/**
 * Enumeration representing workflow approval status.
 */
@Getter
public enum WorkflowStatus {
    PENDING("pending", "Awaiting action"),
    IN_PROGRESS("in_progress", "Being processed"),
    APPROVED("approved", "Approved"),
    REJECTED("rejected", "Rejected"),
    CANCELLED("cancelled", "Cancelled");

    private final String code;
    private final String description;

    WorkflowStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static WorkflowStatus fromCode(String code) {
        for (WorkflowStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown workflow status: " + code);
    }

    public boolean isFinal() {
        return this == APPROVED || this == REJECTED || this == CANCELLED;
    }
}
