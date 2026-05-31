package com.gogidix.finance.compliance.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetComplianceCheckQuery {

    private String tenantId;
    private String id;
}
