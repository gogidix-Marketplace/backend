package com.gogidix.centralconfiguration.configserver.domain.port.in;

import lombok.Builder;

import java.util.List;

/**
 * Query for searching configuration entries with filters and pagination.
 */
@Builder
public record SearchConfigsQuery(
        String tenantId,

        String applicationName,

        List<String> profiles,

        String configKeyPattern,

        Boolean isActive,

        Boolean includeHistory,

        Integer page,

        Integer size,

        String sortBy,

        String sortDirection
) {
    public static SearchConfigsQuery defaults() {
        return SearchConfigsQuery.builder()
                .page(0)
                .size(20)
                .sortBy("createdAt")
                .sortDirection("DESC")
                .isActive(true)
                .build();
    }
}
