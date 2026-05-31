package com.gogidix.globalbusinessmanagement.datavalidation.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetDataQualityReportQuery {

    private String tenantId;
    private String id;
}
