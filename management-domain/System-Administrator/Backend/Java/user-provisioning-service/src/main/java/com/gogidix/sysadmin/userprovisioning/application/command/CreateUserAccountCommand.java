package com.gogidix.sysadmin.userprovisioning.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserAccountCommand {

    private String id;
    private String tenantId;
}
