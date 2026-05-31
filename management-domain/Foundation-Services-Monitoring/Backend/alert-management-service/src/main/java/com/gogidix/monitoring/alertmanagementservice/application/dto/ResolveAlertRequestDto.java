package com.gogidix.monitoring.alertmanagementservice.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for resolving an alert.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResolveAlertRequestDto {

    @NotBlank(message = "User ID is required")
    private String userId;

    private String comment;
}
