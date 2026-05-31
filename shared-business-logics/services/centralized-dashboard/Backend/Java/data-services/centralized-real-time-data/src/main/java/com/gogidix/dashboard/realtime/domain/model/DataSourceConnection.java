package com.gogidix.dashboard.realtime.domain.model;

import java.time.LocalDateTime;

/**
 * Data Source Connection - represents a connection to a real-time data source
 */
public class DataSourceConnection {
    private final String connectionId;
    private final String sourceUrl;
    private final boolean isActive;
    private final LocalDateTime connectedAt;

    public DataSourceConnection(String connectionId, String sourceUrl) {
        this.connectionId = connectionId;
        this.sourceUrl = sourceUrl;
        this.isActive = true;
        this.connectedAt = LocalDateTime.now();
    }

    public String getConnectionId() {
        return connectionId;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public boolean isActive() {
        return isActive;
    }

    /**
     * Check if connection is established
     */
    public boolean isConnected() {
        return isActive;
    }

    public LocalDateTime getConnectedAt() {
        return connectedAt;
    }

    public void connect() {
        // Connection logic
    }

    public void disconnect() {
        // Disconnection logic
    }
}
