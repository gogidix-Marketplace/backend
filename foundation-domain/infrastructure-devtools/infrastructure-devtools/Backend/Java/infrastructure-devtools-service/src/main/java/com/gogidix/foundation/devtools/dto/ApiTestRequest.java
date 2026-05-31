package com.gogidix.foundation.devtools.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * DTO for ad-hoc API test requests.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiTestRequest {

    private String name;
    private String method;
    private String url;
    private Map<String, String> headers;
    private String body;
    private Integer expectedStatusCode;
    private Integer timeout;
    private String validationScript;
}
