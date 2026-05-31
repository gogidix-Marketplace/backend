package com.gogidix.hr.employeeselfservice.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetQuickActionQuery {

    private String tenantId;
    private String id;
}
