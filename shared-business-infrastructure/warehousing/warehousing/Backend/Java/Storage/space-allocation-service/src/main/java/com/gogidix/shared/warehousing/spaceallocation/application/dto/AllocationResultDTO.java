package com.gogidix.shared.warehousing.spaceallocation.application.dto;

import com.gogidix.shared.warehousing.spaceallocation.domain.entity.SpaceAllocation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * DTO for Allocation Result
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AllocationResultDTO {

    private Boolean success;
    private String allocationId;
    private String warehouseId;
    private String zoneId;
    private SpaceAllocation.Location location;
    private String message;
    private Map<String, Object> details;
}
