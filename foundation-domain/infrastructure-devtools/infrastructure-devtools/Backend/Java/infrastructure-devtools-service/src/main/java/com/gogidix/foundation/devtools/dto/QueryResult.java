package com.gogidix.foundation.devtools.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * DTO for database query results.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryResult {

    private UUID queryUuid;
    private String queryName;
    private String databaseName;
    private String status;
    private List<Map<String, Object>> rows;
    private Integer rowsAffected;
    private Integer rowsReturned;
    private Boolean truncated;
    private Long executionTime;
    private String errorMessage;
}
