package com.gogidix.shared.warehousing.access.application.dto;

import com.gogidix.shared.warehousing.access.domain.entity.AccessLog;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Command to log access
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogAccessCommand {

    @NotBlank(message = "Warehouse ID is required")
    private String warehouseId;

    private String zoneId;

    private String accessRequestId;

    @NotBlank(message = "User ID is required")
    private String userId;

    private String userName;

    private String userType;

    @NotNull(message = "Access type is required")
    private AccessLog.AccessType accessType;

    @NotBlank(message = "Result is required")
    private AccessLog.AccessResult result;

    private String failureReason;

    private String badgeId;

    private String gateId;

    private Map<String, Object> metadata;

    private String referenceType;

    private String referenceId;
}
