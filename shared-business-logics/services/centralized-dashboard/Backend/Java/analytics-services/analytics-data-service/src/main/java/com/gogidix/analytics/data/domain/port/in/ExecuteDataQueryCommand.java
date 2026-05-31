package com.gogidix.analytics.data.domain.port.in;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Input port: Command to execute a data query.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecuteDataQueryCommand {

    private String queryId;

    private String queryDefinition;

    @NotNull(message = "Query type is required")
    private com.gogidix.analytics.data.domain.model.DataQuery.QueryType queryType;

    private Map<String, Object> parameters;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Integer limit;

    private Integer offset;

    private String orderBy;

    @Builder.Default
    private Boolean async = false;
}
