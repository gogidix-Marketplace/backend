package com.gogidix.globalbusinessmanagement.datavalidation.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * DTO for validation request
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationRequestDTO {

    @NotBlank(message = "Entity type is required")
    private String entityType;

    @NotBlank(message = "Entity ID is required")
    private String entityId;

    private String entityData;

    @NotNull(message = "Rules to apply are required")
    @NotEmpty(message = "At least one rule must be specified")
    private List<@NotBlank String> ruleCodes;

    private Map<String, Object> context;

    private String tenantId;

    private String validatedBy;

    private boolean async;
}
