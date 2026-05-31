package com.gogidix.customersupport.slamanagement.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetSLABreachQuery {

    private String tenantId;
    private String id;
}
