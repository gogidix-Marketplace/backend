package com.gogidix.centralconfiguration.configserver.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating a new configuration entry.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request DTO for creating a configuration entry")
public class CreateConfigRequestDto {

    @NotBlank(message = "Application name is required")
    @Size(max = 255, message = "Application name must not exceed 255 characters")
    @Schema(description = "Name of the application", example = "payment-service")
    private String applicationName;

    @NotBlank(message = "Profile is required")
    @Size(max = 100, message = "Profile must not exceed 100 characters")
    @Schema(description = "Environment profile", example = "prod")
    private String profile;

    @NotBlank(message = "Configuration key is required")
    @Size(max = 500, message = "Configuration key must not exceed 500 characters")
    @Schema(description = "Configuration key", example = "database.connection.timeout")
    private String configKey;

    @Schema(description = "Configuration value", example = "30000")
    private String configValue;

    @Builder.Default
    @Schema(description = "Whether the value is encrypted", example = "false")
    private Boolean isEncrypted = false;

    @Schema(description = "Configuration description", example = "Database connection timeout in milliseconds")
    private String description;
}
