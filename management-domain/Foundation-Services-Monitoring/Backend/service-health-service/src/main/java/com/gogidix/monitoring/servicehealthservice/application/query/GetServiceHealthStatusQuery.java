package com.gogidix.monitoring.servicehealthservice.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetServiceHealthStatusQuery {

    private String tenantId;
    private String id;
}
