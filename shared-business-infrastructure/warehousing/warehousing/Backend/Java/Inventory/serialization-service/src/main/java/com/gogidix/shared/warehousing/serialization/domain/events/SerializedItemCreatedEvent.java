package com.gogidix.shared.warehousing.serialization.domain.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Event published when a serialized item is created
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SerializedItemCreatedEvent {

    private String serializedItemId;
    private String serialNumber;
    private String sku;
    private String status;
    private String batchId;
    private LocalDate expiryDate;
    private String locationId;
    private String tenantId;
    private LocalDateTime timestamp;
}
