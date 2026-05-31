package com.gogidix.analytics.data.domain.port.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Input port: Command to create a saved data query.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateDataQueryCommand {

    @NotBlank(message = "Query name is required")
    private String queryName;

    private String description;

    @NotBlank(message = "Query definition is required")
    private String queryDefinition;

    @NotNull(message = "Query type is required")
    private com.gogidix.analytics.data.domain.model.DataQuery.QueryType queryType;

    private String dataSource;

    private String parametersSchema;

    private String category;

    private String tags;

    @NotNull(message = "Owner ID is required")
    private String ownerId;

    @Builder.Default
    private Boolean isPublic = false;
}
