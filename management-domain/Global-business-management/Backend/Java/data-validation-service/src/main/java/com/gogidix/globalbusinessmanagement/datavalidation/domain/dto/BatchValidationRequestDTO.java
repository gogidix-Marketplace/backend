package com.gogidix.globalbusinessmanagement.datavalidation.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

/**
 * DTO for batch validation request
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchValidationRequestDTO {

    @NotEmpty(message = "At least one entity must be provided")
    private List<@Valid ValidationRequestDTO> requests;

    private String batchId;

    private String tenantId;

    private int priority;
}
