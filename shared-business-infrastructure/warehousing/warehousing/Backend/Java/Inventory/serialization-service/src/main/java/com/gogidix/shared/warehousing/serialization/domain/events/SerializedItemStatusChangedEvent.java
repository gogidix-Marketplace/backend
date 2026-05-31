package com.gogidix.shared.warehousing.serialization.domain.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Event published when a serialized item status is changed
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SerializedItemStatusChangedEvent {

    private String serializedItemId;
    private String serialNumber;
    private String sku;
    private String oldStatus;
    private String newStatus;
    private String changedBy;
    private String tenantId;
    private LocalDateTime timestamp;
}
