package com.gogidix.sysadmin.alertmanagement.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteAlertRuleCommand {

    private String id;
    private String tenantId;
}
