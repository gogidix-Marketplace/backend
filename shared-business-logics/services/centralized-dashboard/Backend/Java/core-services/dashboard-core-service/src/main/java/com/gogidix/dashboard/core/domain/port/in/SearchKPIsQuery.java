package com.gogidix.dashboard.core.domain.port.in;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Input port query for searching KPIs.
 */
@Data
@Builder
public class SearchKPIsQuery {

    private String tenantId;

    private String category;

    private String sourceDomain;

    private String searchQuery;

    private List<String> codes;

    @Builder.Default
    private Boolean isActive = null;

    @Builder.Default
    private Integer page = 0;

    @Builder.Default
    private Integer size = 20;

    @Builder.Default
    private String sortBy = "name";

    @Builder.Default
    private String sortDirection = "ASC";
}
