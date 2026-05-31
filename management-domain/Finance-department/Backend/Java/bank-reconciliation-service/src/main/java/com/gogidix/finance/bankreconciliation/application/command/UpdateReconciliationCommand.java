package com.gogidix.finance.bankreconciliation.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateReconciliationCommand {

    private String id;
    private String tenantId;
}
