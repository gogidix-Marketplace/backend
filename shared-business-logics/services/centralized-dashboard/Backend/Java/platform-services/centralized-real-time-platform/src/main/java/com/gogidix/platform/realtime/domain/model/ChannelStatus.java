package com.gogidix.platform.realtime.domain.model;

import lombok.Getter;

/**
 * Enumeration of channel statuses.
 */
@Getter
public enum ChannelStatus {
    ACTIVE("active", "Channel is active and accepting messages"),
    INACTIVE("inactive", "Channel is inactive"),
    SUSPENDED("suspended", "Channel is suspended for policy reasons"),
    CLOSED("closed", "Channel is permanently closed");

    private final String code;
    private final String description;

    ChannelStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
