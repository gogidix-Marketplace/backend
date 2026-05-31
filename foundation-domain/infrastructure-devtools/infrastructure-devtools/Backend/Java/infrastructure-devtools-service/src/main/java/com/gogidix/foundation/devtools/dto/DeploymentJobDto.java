package com.gogidix.foundation.devtools.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

/**
 * DTO for deployment job operations.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeploymentJobDto {

    private Long id;
    private UUID uuid;
    private String name;
    private String description;
    private String projectId;
    private String type;
    private String targetEnvironment;
    private String deploymentScript;
    private String preDeploymentScript;
    private String postDeploymentScript;
    private String rollbackScript;
    private Map<String, Object> configuration;
    private Integer timeout;
    private Integer retryCount;
    private Boolean enabled;
    private String tags;
}
