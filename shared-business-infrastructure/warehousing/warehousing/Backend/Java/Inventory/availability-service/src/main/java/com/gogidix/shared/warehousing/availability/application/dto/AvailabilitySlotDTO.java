package com.gogidix.shared.warehousing.availability.application.dto;

import com.gogidix.shared.warehousing.availability.domain.entity.AvailabilitySlot;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for Availability Slot
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvailabilitySlotDTO {

    private String id;
    private String tenantId;
    private String warehouseId;
    private String zoneId;
    private AvailabilitySlot.SlotType slotType;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer capacity;
    private Integer reserved;
    private Integer available;
    private AvailabilitySlot.SlotStatus status;
    private String reservationId;
    private LocalDateTime createdAt;
}
