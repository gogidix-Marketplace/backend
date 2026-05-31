package com.gogidix.customersupport.qualitymanagement.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetScorecardTemplateQuery {

    private String tenantId;
    private String id;
}
