package com.gogidix.globalbusinessmanagement.datavalidation.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for validation response
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationResponseDTO {

    private String validationId;

    private boolean success;

    private boolean passed;

    private int totalRules;

    private int passedRules;

    private int failedRules;

    private List<ValidationResultDTO> results;

    private String message;

    private long executionTimeMs;
}
