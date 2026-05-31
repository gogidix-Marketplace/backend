package com.gogidix.customersupport.countrysupportdashboard.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCountrySpecificMetricsCommand {

    private String id;
    private String tenantId;
}
