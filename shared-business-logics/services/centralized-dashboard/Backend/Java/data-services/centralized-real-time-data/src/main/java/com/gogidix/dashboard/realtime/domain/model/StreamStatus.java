package com.gogidix.dashboard.realtime.domain.model;

/**
 * Stream Status Enumeration
 * 
 * Represents the current status of a real-time data stream
 */
public enum StreamStatus {
    CREATED("Stream has been created but not started"),
    INITIALIZING("Stream is being initialized"),
    ACTIVE("Stream is actively processing data"),
    STOPPED("Stream has been stopped"),
    ERROR("Stream encountered an error"),
    PAUSED("Stream is temporarily paused");
    
    private final String description;
    
    StreamStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean isActive() {
        return this == ACTIVE;
    }
    
    public boolean canStart() {
        return this == CREATED || this == INITIALIZING || this == STOPPED || this == PAUSED;
    }
    
    public boolean canStop() {
        return this == ACTIVE || this == PAUSED;
    }
}