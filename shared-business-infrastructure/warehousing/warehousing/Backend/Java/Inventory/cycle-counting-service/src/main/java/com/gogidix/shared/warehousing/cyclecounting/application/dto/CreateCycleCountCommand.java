package com.gogidix.shared.warehousing.cyclecounting.application.dto;

import com.gogidix.shared.warehousing.cyclecounting.domain.entity.CycleCount;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Command to create cycle count
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCycleCountCommand {

    @NotNull(message = "Warehouse ID is required")
    private String warehouseId;

    private String zoneId;

    @NotNull(message = "Count type is required")
    private CycleCount.CountType countType;

    @Builder.Default
    private CycleCount.CountPriority priority = CycleCount.CountPriority.MEDIUM;

    @NotNull(message = "Scheduled date is required")
    private LocalDateTime scheduledDate;

    private LocalDateTime dueDate;

    private String assignedTo;

    @NotNull(message = "Created by is required")
    private String createdBy;

    private Integer totalItems;

    private String notes;
}
