package com.gogidix.sysadmin.accesscontrol.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetAccessPolicyQuery {

    private String tenantId;
    private String id;
}
