package com.gogidix.finance.exchangerate.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateHistoricalRateCommand {

    private String id;
    private String tenantId;
}
