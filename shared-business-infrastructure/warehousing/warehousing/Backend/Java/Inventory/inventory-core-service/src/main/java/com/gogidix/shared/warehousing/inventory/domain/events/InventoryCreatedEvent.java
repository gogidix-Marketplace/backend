package com.gogidix.shared.warehousing.inventory.domain.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Domain event published when inventory is created
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryCreatedEvent {

    private String eventId;
    private String inventoryId;
    private String sku;
    private Integer quantity;
    private String locationId;
    private String tenantId;
    private LocalDateTime timestamp;

    public static InventoryCreatedEventBuilder builder() {
        return new InventoryCreatedEventBuilder()
            .eventId(java.util.UUID.randomUUID().toString())
            .timestamp(LocalDateTime.now());
    }
}
