package com.gogidix.customersupport.feedback.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteNPSMetricCommand {

    private String id;
    private String tenantId;
}
