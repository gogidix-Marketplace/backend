package com.gogidix.platform.realtime.domain.model;

import lombok.Getter;

/**
 * Enumeration of channel types.
 */
@Getter
public enum ChannelType {
    PUBLIC("public", "Public channel accessible to all authenticated users"),
    PRIVATE("private", "Private channel accessible to specific users"),
    SYSTEM("system", "System channel for internal messages"),
    DATA("data", "Data channel for real-time data streaming");

    private final String code;
    private final String description;

    ChannelType(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
