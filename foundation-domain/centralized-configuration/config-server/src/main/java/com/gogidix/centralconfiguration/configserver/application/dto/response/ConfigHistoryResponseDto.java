package com.gogidix.centralconfiguration.configserver.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for configuration history response.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response DTO for configuration history entry")
public class ConfigHistoryResponseDto {

    @Schema(description = "History entry ID")
    private Long id;

    @Schema(description = "Original configuration ID")
    private Long configurationId;

    @Schema(description = "Tenant ID")
    private String tenantId;

    @Schema(description = "Application name")
    private String applicationName;

    @Schema(description = "Environment profile")
    private String profile;

    @Schema(description = "Configuration key")
    private String configKey;

    @Schema(description = "Previous value")
    private String oldValue;

    @Schema(description = "New value")
    private String newValue;

    @Schema(description = "Configuration version")
    private Integer version;

    @Schema(description = "Type of change (CREATE, UPDATE, DELETE)")
    private String changeType;

    @Schema(description = "User who made the change")
    private String changedBy;

    @Schema(description = "Reason for the change")
    private String changeReason;

    @Schema(description = "Change timestamp")
    private LocalDateTime createdAt;
}
