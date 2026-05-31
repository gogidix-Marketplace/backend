package com.gogidix.foundation.devtools.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

/**
 * DTO for API test case operations.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiTestCaseDto {

    private Long id;
    private UUID uuid;
    private String name;
    private String description;
    private String projectId;
    private String method;
    private String url;
    private Map<String, String> headers;
    private String requestBody;
    private Integer expectedStatusCode;
    private String expectedResponseBody;
    private String validationScript;
    private Boolean enabled;
    private String environment;
    private String tags;
    private Integer timeout;
    private String createdBy;
    private String updatedBy;
}
