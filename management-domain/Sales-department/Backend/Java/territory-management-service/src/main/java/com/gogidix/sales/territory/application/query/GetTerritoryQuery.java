package com.gogidix.sales.territory.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetTerritoryQuery {

    private String tenantId;
    private String id;
}
