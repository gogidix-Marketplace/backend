package com.gogidix.sysadmin.accesscontrol.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteAccessPolicyCommand {

    private String id;
    private String tenantId;
}
