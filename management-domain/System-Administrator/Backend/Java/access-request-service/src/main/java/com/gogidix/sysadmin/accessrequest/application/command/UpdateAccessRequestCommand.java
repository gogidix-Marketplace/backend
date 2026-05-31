package com.gogidix.sysadmin.accessrequest.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAccessRequestCommand {

    private String id;
    private String tenantId;
}
