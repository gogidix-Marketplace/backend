package com.gogidix.sysadmin.incidentmanagement.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetIncidentQuery {

    private String tenantId;
    private String id;
}
