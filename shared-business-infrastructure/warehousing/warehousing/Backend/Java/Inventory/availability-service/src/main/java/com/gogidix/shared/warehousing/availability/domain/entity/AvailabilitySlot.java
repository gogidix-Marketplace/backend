package com.gogidix.shared.warehousing.availability.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Availability Slot Entity
 *
 * Represents a specific time slot for availability
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "availability_slots")
@CompoundIndex(def = "{'tenantId': 1, 'warehouseId': 1, 'startTime': 1}", name = "idx_slot_tenant_time")
public class AvailabilitySlot {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    @Indexed
    private String warehouseId;

    private String zoneId;

    private SlotType slotType;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer capacity;

    private Integer reserved;

    private Integer available;

    private SlotStatus status;

    private String reservationId;

    @CreatedDate
    private LocalDateTime createdAt;

    public enum SlotType {
        PICKING,
        RECEIVING,
        SHIPPING,
        ACCESS,
        MAINTENANCE
    }

    public enum SlotStatus {
        AVAILABLE,
        RESERVED,
        FULL,
        CANCELLED,
        EXPIRED
    }

    /**
     * Update availability count
     */
    public void updateAvailability() {
        this.available = capacity - reserved;
        if (available <= 0) {
            this.status = SlotStatus.FULL;
        } else {
            this.status = SlotStatus.AVAILABLE;
        }
    }
}
