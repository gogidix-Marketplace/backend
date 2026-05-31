package com.gogidix.shared.warehousing.availability.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for Reservation Result
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResultDTO {

    private Boolean success;
    private String reservationId;
    private String warehouseId;
    private String zoneId;
    private String poolId;
    private Integer reservedQuantity;
    private String message;
    private LocalDateTime expiresAt;
    private LocalDateTime reservedAt;
}
