package com.gogidix.shared.warehousing.putaway.domain.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "putaway_tasks")
@CompoundIndex(def = "{'tenantId': 1, 'receiptId': 1}", name = "idx_tenant_receipt")
@Schema(description = "Put-away task entity")
public class PutawayTask {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    @Indexed
    private String taskNumber;

    @Indexed
    private String receiptId;

    private String receiptLineNumber;

    @Indexed
    private TaskStatus status;

    private String sku;

    private String productName;

    private Integer quantity;

    private String fromLocation;

    private PutawayLocation suggestedLocation;

    private String assignedLocation;

    private Integer priority;

    private String assignedTo;

    private LocalDateTime dueDate;

    private LocalDateTime completedDate;

    private String notes;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public enum TaskStatus {
        PENDING, ASSIGNED, IN_PROGRESS, COMPLETED, CANCELLED
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PutawayLocation {
        private String locationId;
        private String zone;
        private String aisle;
        private String bay;
        private String level;
        private Double capacityUtilization;
        private Boolean requiresEquipment;
    }
}
