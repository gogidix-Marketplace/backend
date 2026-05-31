package com.gogidix.globalbusinessmanagement.businessintelligence.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetTrendAnalysisQuery {

    private String tenantId;
    private String id;
}
