package com.gogidix.sysadmin.userprovisioning.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetUserAccountQuery {

    private String tenantId;
    private String id;
}
