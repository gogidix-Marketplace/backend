package com.gogidix.sysadmin.environment.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetEnvironmentQuery {

    private String tenantId;
    private String id;
}
