package com.gogidix.management.executive.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePerformanceBenchmarkCommand {

    private String id;
    private String tenantId;
}
