package com.gogidix.foundation.devtools.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * DTO for deployment requests.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeploymentRequest {

    private String executedBy;
    private String version;
    private String commitSha;
    private Map<String, String> environment;
    private Map<String, String> parameters;
}
