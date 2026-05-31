package com.gogidix.dashboard.aggregation.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Aggregation Source Value Object
 * 
 * Represents the source of data for aggregation
 */
public class AggregationSource {
    private final String sourceId;
    private final String sourceName;
    private final SourceType type;
    private final String connectionString;
    private final LocalDateTime lastUpdated;
    private final boolean isReliable;
    
    public AggregationSource(String sourceId, String sourceName, SourceType type, 
                           String connectionString, LocalDateTime lastUpdated, boolean isReliable) {
        this.sourceId = Objects.requireNonNull(sourceId, "Source ID cannot be null");
        this.sourceName = Objects.requireNonNull(sourceName, "Source name cannot be null");
        this.type = Objects.requireNonNull(type, "Source type cannot be null");
        this.connectionString = connectionString;
        this.lastUpdated = Objects.requireNonNull(lastUpdated, "Last updated cannot be null");
        this.isReliable = isReliable;
    }
    
    public static AggregationSource database(String sourceId, String sourceName, String connectionString) {
        return new AggregationSource(sourceId, sourceName, SourceType.DATABASE, 
                                   connectionString, LocalDateTime.now(), true);
    }
    
    public static AggregationSource api(String sourceId, String sourceName, String endpoint) {
        return new AggregationSource(sourceId, sourceName, SourceType.API, 
                                   endpoint, LocalDateTime.now(), true);
    }
    
    public static AggregationSource stream(String sourceId, String sourceName) {
        return new AggregationSource(sourceId, sourceName, SourceType.STREAM, 
                                   null, LocalDateTime.now(), false);
    }
    
    // Getters
    public String getSourceId() { return sourceId; }
    public String getSourceName() { return sourceName; }
    public SourceType getType() { return type; }
    public String getConnectionString() { return connectionString; }
    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public boolean isReliable() { return isReliable; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AggregationSource that = (AggregationSource) o;
        return Objects.equals(sourceId, that.sourceId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(sourceId);
    }
    
    public enum SourceType {
        DATABASE("Database"),
        API("API"),
        STREAM("Stream"),
        FILE("File"),
        CACHE("Cache");
        
        private final String displayName;
        
        SourceType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() { return displayName; }
    }
}