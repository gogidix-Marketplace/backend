package com.gogidix.foundation.devtools.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

/**
 * DTO for documentation project operations.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocumentationProjectDto {

    private Long id;
    private UUID uuid;
    private String name;
    private String description;
    private String projectId;
    private String sourceUrl;
    private String sourcePath;
    private Map<String, Object> configuration;
    private String outputFormat;
    private Boolean enabled;
    private Integer autoGenerateInterval;
}
