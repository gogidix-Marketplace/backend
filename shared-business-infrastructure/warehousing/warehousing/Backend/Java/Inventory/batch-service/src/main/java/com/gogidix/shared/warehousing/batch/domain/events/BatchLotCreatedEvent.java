package com.gogidix.shared.warehousing.batch.domain.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Event published when a batch lot is created
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchLotCreatedEvent {

    private String lotId;
    private String lotNumber;
    private String sku;
    private Integer totalQuantity;
    private LocalDate expirationDate;
    private String supplierId;
    private String tenantId;
    private LocalDateTime timestamp;
}
