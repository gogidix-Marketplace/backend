package com.gogidix.globalbusinessmanagement.businessintelligence.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetInsightQuery {

    private String tenantId;
    private String id;
}
