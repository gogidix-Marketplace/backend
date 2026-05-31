package com.gogidix.platform.platform.domain.port.in;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Input port: Command to create maintenance window.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateMaintenanceWindowCommand {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    private LocalDateTime endTime;

    @NotNull(message = "Maintenance type is required")
    private com.gogidix.platform.platform.domain.model.MaintenanceWindow.MaintenanceType maintenanceType;

    @NotNull(message = "Impact level is required")
    private com.gogidix.platform.platform.domain.model.MaintenanceWindow.ImpactLevel impactLevel;

    private List<String> affectedServices;

    private List<String> affectedRegions;

    private String notificationMessage;
}
