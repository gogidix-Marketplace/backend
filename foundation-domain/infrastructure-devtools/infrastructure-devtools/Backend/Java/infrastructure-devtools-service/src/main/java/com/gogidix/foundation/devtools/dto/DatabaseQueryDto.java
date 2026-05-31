package com.gogidix.foundation.devtools.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

/**
 * DTO for database query operations.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DatabaseQueryDto {

    private Long id;
    private UUID uuid;
    private String name;
    private String description;
    private String projectId;
    private String databaseName;
    private String query;
    private String queryType;
    private Boolean enabled;
    private String tags;
    private Map<String, Object> parameters;
    private Integer maxRows;
    private Integer timeoutSeconds;
}
