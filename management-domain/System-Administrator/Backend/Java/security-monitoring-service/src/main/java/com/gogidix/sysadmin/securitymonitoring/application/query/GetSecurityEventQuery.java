package com.gogidix.sysadmin.securitymonitoring.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetSecurityEventQuery {

    private String tenantId;
    private String id;
}
