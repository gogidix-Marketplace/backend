package com.gogidix.shared.warehousing.batch.domain.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Event published when a batch is created
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchCreatedEvent {

    private String batchId;
    private String batchNumber;
    private String sku;
    private Integer totalQuantity;
    private String tenantId;
    private LocalDateTime timestamp;
}
