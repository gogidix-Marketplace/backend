package com.gogidix.customersupport.supportanalytics.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTicketTrendCommand {

    private String id;
    private String tenantId;
}
