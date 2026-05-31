package com.gogidix.sysadmin.audit.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetAuditLogQuery {

    private String tenantId;
    private String id;
}
