package com.gogidix.sysadmin.audit.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteAuditLogCommand {

    private String id;
    private String tenantId;
}
