package com.gogidix.monitoring.performance.domain.model;

import lombok.Getter;

/**
 * Enumeration of alert statuses.
 */
@Getter
public enum AlertStatus {
    OPEN("open", "Alert is active and unresolved"),
    ACKNOWLEDGED("acknowledged", "Alert has been acknowledged"),
    RESOLVED("resolved", "Alert has been resolved"),
    SILENCED("silenced", "Alert is temporarily silenced");

    private final String code;
    private final String description;

    AlertStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
