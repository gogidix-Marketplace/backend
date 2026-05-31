package com.gogidix.finance.accountsreceivable.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeletePaymentScheduleCommand {

    private String id;
    private String tenantId;
}
