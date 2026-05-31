package com.gogidix.foundation.devtools.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

/**
 * DTO for deployment execution results.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeploymentResult {

    private UUID jobUuid;
    private String jobName;
    private String environment;
    private String status;
    private String version;
    private Long duration;
    private String outputLog;
    private String errorLog;
    private String errorMessage;
}
