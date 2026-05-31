package com.gogidix.foundation.devtools.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * DTO for API test execution results.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiTestResult {

    private UUID testCaseUuid;
    private String testCaseName;
    private String method;
    private String url;
    private String status;
    private Integer statusCode;
    private Boolean statusMatch;
    private Boolean bodyValid;
    private Boolean scriptValid;
    private Boolean passed;
    private String responseBody;
    private Long responseTime;
    private String errorMessage;
    private List<Map<String, Object>> assertions;
}
