package com.gogidix.shared.warehousing.cyclecounting.application.dto;

import com.gogidix.shared.warehousing.cyclecounting.domain.entity.CycleCount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for Cycle Count
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CycleCountDTO {

    private String id;
    private String tenantId;
    private String warehouseId;
    private String zoneId;
    private String countNumber;
    private CycleCount.CountType countType;
    private CycleCount.CountPriority priority;
    private CycleCount.CountStatus status;
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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
