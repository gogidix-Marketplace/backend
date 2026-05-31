package com.gogidix.customersupport.ticketmanagement.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteTicketAuditLogCommand {

    private String id;
    private String tenantId;
}
