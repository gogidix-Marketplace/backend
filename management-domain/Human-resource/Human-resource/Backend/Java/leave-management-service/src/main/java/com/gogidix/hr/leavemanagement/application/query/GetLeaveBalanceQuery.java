package com.gogidix.hr.leavemanagement.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetLeaveBalanceQuery {

    private String tenantId;
    private String id;
}
