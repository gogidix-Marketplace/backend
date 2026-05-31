package com.gogidix.hr.globalcompliance.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetAuditTrailQuery {

    private String tenantId;
    private String id;
}
