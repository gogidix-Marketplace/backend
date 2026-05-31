package com.gogidix.platform.platform.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for maintenance window responses.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceWindowDto {

    private String id;
    private String tenantId;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String maintenanceType;
    private String impactLevel;
    private boolean isActive;
    private String status;
    private List<String> affectedServices;
    private List<String> affectedRegions;
    private String notificationMessage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
