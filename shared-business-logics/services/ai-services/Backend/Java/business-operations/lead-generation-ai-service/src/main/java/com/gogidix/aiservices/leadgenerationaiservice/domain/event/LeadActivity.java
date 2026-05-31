package com.gogidix.aiservices.leadgenerationaiservice.domain.event;

import com.gogidix.aiservices.leadgenerationaiservice.domain.model.ActivityType;
import lombok.Builder;

import java.time.Instant;
import java.util.*;

@Builder
public class LeadActivity {
    private final UUID activityId;
    private final UUID leadId;
    private final ActivityType type;
    private final String description;
    private final Instant timestamp;
    private final String createdBy;
    @Builder.Default
    private final Map<String, Object> metadata = new HashMap<>();
    private final String notes;

    private LeadActivity(UUID activityId, UUID leadId, ActivityType type,
                        String description, Instant timestamp, String createdBy,
                        Map<String, Object> metadata, String notes) {
        if (type == null) {
            throw new IllegalArgumentException("Activity type cannot be null");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty");
        }
        if (timestamp == null) {
            throw new IllegalArgumentException("Timestamp cannot be null");
        }
        this.activityId = activityId != null ? activityId : UUID.randomUUID();
        this.leadId = leadId;
        this.type = type;
        this.description = description;
        this.timestamp = timestamp;
        this.createdBy = createdBy;
        this.metadata = metadata != null ? new HashMap<>(metadata) : new HashMap<>();
        this.notes = notes;
    }

    public UUID getActivityId() {
        return activityId;
    }

    public UUID getLeadId() {
        return leadId;
    }

    public ActivityType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Map<String, Object> getMetadata() {
        return Collections.unmodifiableMap(metadata);
    }

    public String getNotes() {
        return notes;
    }

    public boolean isEngagementActivity() {
        return type.isEngagement();
    }

    public boolean isOutreachActivity() {
        return type.isOutreach();
    }

    public boolean isRecent() {
        return timestamp.isAfter(Instant.now().minusSeconds(7 * 24 * 60 * 60));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LeadActivity that = (LeadActivity) o;
        return Objects.equals(activityId, that.activityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(activityId);
    }

    @Override
    public String toString() {
        return "LeadActivity{" +
                "type=" + type +
                ", description='" + description + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
