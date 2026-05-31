package com.gogidix.centralconfiguration.configserver.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for configuration response.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response DTO for a configuration entry")
public class ConfigurationResponseDto {

    @Schema(description = "Configuration ID")
    private Long id;

    @Schema(description = "Tenant ID")
    private String tenantId;

    @Schema(description = "Application name")
    private String applicationName;

    @Schema(description = "Environment profile")
    private String profile;

    @Schema(description = "Configuration key")
    private String configKey;

    @Schema(description = "Configuration value (may be masked if encrypted)")
    private String configValue;

    @Schema(description = "Whether the value is encrypted")
    private Boolean isEncrypted;

    @Schema(description = "Configuration version")
    private Integer version;

    @Schema(description = "Whether the configuration is active")
    private Boolean isActive;

    @Schema(description = "Configuration description")
    private String description;

    @Schema(description = "Creator user ID")
    private String createdBy;

    @Schema(description = "Updater user ID")
    private String updatedBy;

    @Schema(description = "Creation timestamp")
    private LocalDateTime createdAt;

    @Schema(description = "Last update timestamp")
    private LocalDateTime updatedAt;
}
