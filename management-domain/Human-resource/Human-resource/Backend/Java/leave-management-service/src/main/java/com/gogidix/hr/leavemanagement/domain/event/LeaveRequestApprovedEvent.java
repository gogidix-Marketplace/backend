package com.gogidix.hr.leavemanagement.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequestApprovedEvent {

    private String tenantId;
    private String requestId;
    private String employeeId;
    private String approverId;
    private String approverName;
    private String leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double requestedDays;
    private String approvedAt;
}
