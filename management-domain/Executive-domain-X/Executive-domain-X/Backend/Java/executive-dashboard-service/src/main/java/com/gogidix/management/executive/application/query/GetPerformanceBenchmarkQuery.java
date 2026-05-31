package com.gogidix.management.executive.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetPerformanceBenchmarkQuery {

    private String tenantId;
    private String id;
}
