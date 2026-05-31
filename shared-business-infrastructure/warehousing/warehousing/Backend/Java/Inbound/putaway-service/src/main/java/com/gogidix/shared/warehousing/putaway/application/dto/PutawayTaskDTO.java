package com.gogidix.shared.warehousing.putaway.application.dto;

import com.gogidix.shared.warehousing.putaway.domain.entity.PutawayTask;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PutawayTaskDTO {

    private String id;
    private String tenantId;
    private String taskNumber;
    private String receiptId;
    private PutawayTask.TaskStatus status;
    private String sku;
    private String productName;
    private Integer quantity;
    private String fromLocation;
    private PutawayTask.PutawayLocation suggestedLocation;
    private String assignedLocation;
    private String assignedTo;
    private LocalDateTime dueDate;
    private LocalDateTime completedDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
