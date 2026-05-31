package com.gogidix.shared.warehousing.access.application.dto;

import com.gogidix.shared.warehousing.access.domain.entity.AccessRequest;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Command to request access
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestAccessCommand {

    @NotBlank(message = "Warehouse ID is required")
    private String warehouseId;

    private String zoneId;

    @NotNull(message = "Access type is required")
    private AccessRequest.AccessType accessType;

    @NotNull(message = "Purpose is required")
    private AccessRequest.RequestPurpose purpose;

    @NotBlank(message = "Requested by is required")
    private String requestedBy;

    private String requestedFor;

    private LocalDateTime requestedStartTime;

    private LocalDateTime requestedEndTime;

    @Min(value = 1, message = "Duration must be at least 1 minute")
    private Integer estimatedDurationMinutes;

    private String notes;

    private String referenceType;

    private String referenceId;
}
