package com.gogidix.finance.budgettracking.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateThresholdAlertCommand {

    private String id;
    private String tenantId;
}
