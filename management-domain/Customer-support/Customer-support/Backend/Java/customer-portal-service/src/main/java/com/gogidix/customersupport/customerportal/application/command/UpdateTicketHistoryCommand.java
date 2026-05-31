package com.gogidix.customersupport.customerportal.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTicketHistoryCommand {

    private String id;
    private String tenantId;
}
