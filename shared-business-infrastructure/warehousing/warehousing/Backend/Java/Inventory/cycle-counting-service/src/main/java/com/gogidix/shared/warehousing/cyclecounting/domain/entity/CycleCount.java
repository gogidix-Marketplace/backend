package com.gogidix.shared.warehousing.cyclecounting.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Cycle Count Entity
 *
 * Represents a scheduled cycle count for inventory
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "cycle_counts")
@CompoundIndex(def = "{'tenantId': 1, 'warehouseId': 1, 'scheduledDate': -1}", name = "idx_cycle_count_tenant_date")
public class CycleCount {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    @Indexed
    private String warehouseId;

    private String zoneId;

    @Indexed
    private String countNumber;

    private CountType countType;

    private CountPriority priority;

    private CountStatus status;

    private LocalDateTime scheduledDate;

    private LocalDateTime startedAt;

    private LocalDateTime completedAt;

    private String createdBy;

    private String assignedTo;

    private Integer totalItems;

    private Integer itemsCounted;

    private Integer itemsDiscrepant;

    private Double completionPercentage;

    private String notes;

    private LocalDateTime dueDate;

    private Boolean reconciled;

    private String reconciledBy;

    private LocalDateTime reconciledAt;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public enum CountType {
        FULL,
        PARTIAL,
        ABC_BASED,
        ZONE_BASED,
        SKU_BASED,
        SPOT_CHECK,
        BLIND_COUNT,
        DIRECTED_COUNT
    }

    public enum CountPriority {
        LOW,
        MEDIUM,
        HIGH,
        URGENT
    }

    public enum CountStatus {
        SCHEDULED,
        IN_PROGRESS,
        PAUSED,
        COMPLETED,
        CANCELLED,
        EXPIRED,
        RECONCILED
    }

    /**
     * Start the count
     */
    public void start() {
        this.status = CountStatus.IN_PROGRESS;
        this.startedAt = LocalDateTime.now();
    }

    /**
     * Complete the count
     */
    public void complete() {
        this.status = CountStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
        if (totalItems != null && totalItems > 0) {
            this.completionPercentage = (itemsCounted * 100.0) / totalItems;
        }
    }

    /**
     * Pause the count
     */
    public void pause() {
        this.status = CountStatus.PAUSED;
    }

    /**
     * Resume the count
     */
    public void resume() {
        this.status = CountStatus.IN_PROGRESS;
    }

    /**
     * Cancel the count
     */
    public void cancel() {
        this.status = CountStatus.CANCELLED;
    }

    /**
     * Mark as reconciled
     */
    public void reconcile(String reconciledBy) {
        this.reconciled = true;
        this.reconciledBy = reconciledBy;
        this.reconciledAt = LocalDateTime.now();
        this.status = CountStatus.RECONCILED;
    }

    /**
     * Update count progress
     */
    public void updateProgress(Integer counted, Integer discrepant) {
        this.itemsCounted = counted;
        this.itemsDiscrepant = discrepant;
        if (totalItems != null && totalItems > 0) {
            this.completionPercentage = (counted * 100.0) / totalItems;
        }
    }

    /**
     * Check if count is overdue
     */
    public boolean isOverdue() {
        return dueDate != null && LocalDateTime.now().isAfter(dueDate) &&
               (status == CountStatus.SCHEDULED || status == CountStatus.IN_PROGRESS);
    }
}
