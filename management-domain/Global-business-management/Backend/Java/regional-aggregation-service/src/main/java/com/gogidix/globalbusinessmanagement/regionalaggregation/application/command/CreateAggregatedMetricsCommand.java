package com.gogidix.globalbusinessmanagement.regionalaggregation.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAggregatedMetricsCommand {

    private String id;
    private String tenantId;
}
