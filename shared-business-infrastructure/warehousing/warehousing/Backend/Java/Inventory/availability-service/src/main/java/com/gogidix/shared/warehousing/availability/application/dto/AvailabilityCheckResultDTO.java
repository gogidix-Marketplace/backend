package com.gogidix.shared.warehousing.availability.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * DTO for Availability Check Result
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvailabilityCheckResultDTO {

    private Boolean available;
    private String warehouseId;
    private String zoneId;
    private String poolId;
    private Integer requestedQuantity;
    private Integer availableQuantity;
    private String message;
    private Map<String, Object> details;
}
