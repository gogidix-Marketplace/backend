package com.gogidix.hr.leavemanagement.domain.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveBalanceUpdatedEvent {

    private String tenantId;
    private String balanceId;
    private String employeeId;
    private String leaveType;
    private Double previousBalance;
    private Double newBalance;
    private Double used;
    private Double accrued;
    private String operationType;
    private String timestamp;
}
