package com.gogidix.customersupport.countrysupportdashboard.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetCountrySpecificMetricsQuery {

    private String tenantId;
    private String id;
}
