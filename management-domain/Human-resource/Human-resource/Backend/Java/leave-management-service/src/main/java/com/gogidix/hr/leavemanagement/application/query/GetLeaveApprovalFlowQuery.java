package com.gogidix.hr.leavemanagement.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetLeaveApprovalFlowQuery {

    private String tenantId;
    private String id;
}
