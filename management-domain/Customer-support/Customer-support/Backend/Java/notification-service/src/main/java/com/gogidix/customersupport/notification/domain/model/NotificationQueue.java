package com.gogidix.customersupport.notification.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "notification_queue")
public class NotificationQueue extends BaseEntity {

    @Field("notification_id")
    @Indexed
    private String notificationId;

    @Field("queue_name")
    @Indexed
    private String queueName;

    @Field("priority")
    @Indexed
    private Integer priority;

    @Field("scheduled_at")
    @Indexed
    private Instant scheduledAt;

    @Field("payload")
    private Map<String, Object> payload;

    @Field("processing_status")
    private ProcessingStatus processingStatus;

    @Field("locked_until")
    private Instant lockedUntil;

    @Field("locked_by")
    private String lockedBy;

    @Field("attempt_count")
    private Integer attemptCount;

    @Field("max_attempts")
    private Integer maxAttempts;

    @Field("last_attempt_at")
    private Instant lastAttemptAt;

    @Field("error_message")
    private String errorMessage;

    public static NotificationQueue create(String tenantId, String notificationId, String queueName,
                                            Instant scheduledAt, Map<String, Object> payload) {
        NotificationQueue queue = new NotificationQueue();
        queue.setId(java.util.UUID.randomUUID().toString());
        queue.setTenantId(tenantId);
        queue.setNotificationId(notificationId);
        queue.setQueueName(queueName);
        queue.setScheduledAt(scheduledAt);
        queue.setPayload(payload);
        queue.setProcessingStatus(ProcessingStatus.PENDING);
        queue.setAttemptCount(0);
        queue.setMaxAttempts(3);
        queue.setCreatedAt(Instant.now());
        queue.setUpdatedAt(Instant.now());
        return queue;
    }

    public void lock(String workerId, Instant lockUntil) {
        this.processingStatus = ProcessingStatus.PROCESSING;
        this.lockedBy = workerId;
        this.lockedUntil = lockUntil;
        this.updateTimestamp();
    }

    public void release() {
        this.processingStatus = ProcessingStatus.PENDING;
        this.lockedBy = null;
        this.lockedUntil = null;
        this.updateTimestamp();
    }

    public void markAsCompleted() {
        this.processingStatus = ProcessingStatus.COMPLETED;
        this.lockedBy = null;
        this.lockedUntil = null;
        this.updateTimestamp();
    }

    public void markAsFailed(String error) {
        this.processingStatus = ProcessingStatus.FAILED;
        this.errorMessage = error;
        this.lockedBy = null;
        this.lockedUntil = null;
        this.updateTimestamp();
    }

    public void incrementAttempt() {
        this.attemptCount = (this.attemptCount != null ? this.attemptCount : 0) + 1;
        this.lastAttemptAt = Instant.now();
        this.updateTimestamp();
    }

    public boolean isAvailable() {
        return this.processingStatus == ProcessingStatus.PENDING ||
               (this.lockedUntil != null && Instant.now().isAfter(this.lockedUntil));
    }

    public boolean canRetry() {
        return this.attemptCount < this.maxAttempts;
    }

    public enum ProcessingStatus {
        PENDING, PROCESSING, COMPLETED, FAILED, CANCELLED
    }
}
