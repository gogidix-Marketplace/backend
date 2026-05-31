package com.gogidix.hr.leavemanagement.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequestRejectedEvent {

    private String tenantId;
    private String requestId;
    private String employeeId;
    private String approverId;
    private String reason;
    private String leaveType;
    private String rejectedAt;
}
