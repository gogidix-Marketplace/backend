package com.gogidix.shared.warehousing.cyclecounting.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Count Session Entity
 *
 * Represents a counting session for a cycle count
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "count_sessions")
@CompoundIndex(def = "{'tenantId': 1, 'cycleCountId': 1, 'startedAt': -1}", name = "idx_session_tenant_cycle")
public class CountSession {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    @Indexed
    private String cycleCountId;

    private String sessionNumber;

    @Indexed
    private String counterId;

    private String counterName;

    private SessionStatus status;

    private LocalDateTime startedAt;

    private LocalDateTime endedAt;

    private Integer itemsCounted;

    private Integer itemsDiscrepant;

    private Long durationSeconds;

    private String zoneId;

    private String location;

    private String notes;

    @CreatedDate
    private LocalDateTime createdAt;

    public enum SessionStatus {
        ACTIVE,
        PAUSED,
        COMPLETED,
        CANCELLED
    }

    /**
     * Start the session
     */
    public void start() {
        this.status = SessionStatus.ACTIVE;
        this.startedAt = LocalDateTime.now();
    }

    /**
     * Complete the session
     */
    public void complete() {
        this.status = SessionStatus.COMPLETED;
        this.endedAt = LocalDateTime.now();
        if (this.startedAt != null) {
            this.durationSeconds = java.time.Duration.between(this.startedAt, this.endedAt).getSeconds();
        }
    }

    /**
     * Pause the session
     */
    public void pause() {
        this.status = SessionStatus.PAUSED;
    }

    /**
     * Resume the session
     */
    public void resume() {
        this.status = SessionStatus.ACTIVE;
    }
}
