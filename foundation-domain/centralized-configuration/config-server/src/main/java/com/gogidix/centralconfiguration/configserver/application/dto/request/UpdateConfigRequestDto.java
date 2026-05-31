package com.gogidix.centralconfiguration.configserver.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating a configuration entry.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request DTO for updating a configuration entry")
public class UpdateConfigRequestDto {

    @Schema(description = "New configuration value")
    private String configValue;

    @Schema(description = "Whether the value should be encrypted")
    private Boolean isEncrypted;

    @Schema(description = "Configuration description")
    private String description;

    @Schema(description = "Reason for the change", example = "Updated timeout for better performance")
    private String changeReason;
}
