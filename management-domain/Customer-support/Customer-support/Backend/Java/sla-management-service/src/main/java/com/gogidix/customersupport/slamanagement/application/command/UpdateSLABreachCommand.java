package com.gogidix.customersupport.slamanagement.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSLABreachCommand {

    private String id;
    private String tenantId;
}
