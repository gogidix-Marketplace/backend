package com.gogidix.finance.consolidation.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateConsolidatedBalanceCommand {

    private String id;
    private String tenantId;
}
