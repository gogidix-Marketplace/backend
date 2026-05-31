package com.gogidix.aiservices.aifrauddetectionservice.infrastructure.persistence.mongodb;

import com.gogidix.aiservices.aifrauddetectionservice.domain.model.FraudPattern;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

/**
 * MongoDB document entity for storing FraudPattern domain model.
 * This is the persistence layer representation optimized for MongoDB storage.
 */
@Document(collection = "fraud_patterns")
public class FraudPatternEntity {

    @Id
    private String id;

    @Indexed
    @Field("pattern_id")
    private String patternId;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Field("pattern_name")
    private String patternName;

    @Field("description")
    private String description;

    @Field("confidence_score")
    private double confidenceScore;

    @Field("last_seen")
    private Instant lastSeen;

    @Field("occurrence_count")
    private int occurrenceCount;

    @Field("is_active")
    private boolean isActive;

    @Field("created_at")
    private Instant createdAt;

    @Field("updated_at")
    private Instant updatedAt;

    // Default constructor for MongoDB
    public FraudPatternEntity() {
    }

    // Constructor from domain model
    public FraudPatternEntity(FraudPattern pattern) {
        this.patternId = pattern.getPatternId();
        this.tenantId = pattern.getTenantId();
        this.patternName = pattern.getPatternName();
        this.description = pattern.getDescription();
        this.confidenceScore = pattern.getConfidenceScore();
        this.lastSeen = pattern.getLastSeen();
        this.occurrenceCount = pattern.getOccurrenceCount();
        this.isActive = true;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    // Convert to domain model
    public FraudPattern toDomainModel() {
        return FraudPattern.builder()
                .patternId(this.patternId)
                .tenantId(this.tenantId)
                .patternName(this.patternName)
                .description(this.description)
                .confidenceScore(this.confidenceScore)
                .lastSeen(this.lastSeen)
                .occurrenceCount(this.occurrenceCount)
                .build();
    }

    // Update from domain model
    public void updateFrom(FraudPattern pattern) {
        this.patternName = pattern.getPatternName();
        this.description = pattern.getDescription();
        this.confidenceScore = pattern.getConfidenceScore();
        this.lastSeen = pattern.getLastSeen();
        this.occurrenceCount = pattern.getOccurrenceCount();
        this.updatedAt = Instant.now();
    }

    // Increment occurrence count
    public void incrementOccurrence() {
        this.occurrenceCount++;
        this.lastSeen = Instant.now();
        this.updatedAt = Instant.now();
    }

    // Deactivate pattern
    public void deactivate() {
        this.isActive = false;
        this.updatedAt = Instant.now();
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPatternId() {
        return patternId;
    }

    public void setPatternId(String patternId) {
        this.patternId = patternId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getPatternName() {
        return patternName;
    }

    public void setPatternName(String patternName) {
        this.patternName = patternName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getConfidenceScore() {
        return confidenceScore;
    }

    public void setConfidenceScore(double confidenceScore) {
        this.confidenceScore = confidenceScore;
    }

    public Instant getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(Instant lastSeen) {
        this.lastSeen = lastSeen;
    }

    public int getOccurrenceCount() {
        return occurrenceCount;
    }

    public void setOccurrenceCount(int occurrenceCount) {
        this.occurrenceCount = occurrenceCount;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
