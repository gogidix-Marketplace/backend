package com.gogidix.sysadmin.configuration.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetConfigurationQuery {

    private String tenantId;
    private String id;
}
