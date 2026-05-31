package com.gogidix.hr.employeeselfservice.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSelfServiceRequestCommand {

    private String id;
    private String tenantId;
}
