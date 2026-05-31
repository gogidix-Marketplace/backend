package com.gogidix.foundation.devtools.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for API test execution records.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiTestExecutionDto {

    private Long id;
    private UUID uuid;
    private Long testCaseId;
    private String status;
    private Integer actualStatusCode;
    private String responseBody;
    private String errorMessage;
    private Long responseTime;
    private Long executionTime;
    private String executedBy;
    private LocalDateTime executedAt;
    private String environment;
}
