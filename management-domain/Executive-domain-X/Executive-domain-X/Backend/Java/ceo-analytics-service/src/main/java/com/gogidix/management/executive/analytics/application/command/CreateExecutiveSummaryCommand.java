package com.gogidix.management.executive.analytics.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateExecutiveSummaryCommand {

    private String id;
    private String tenantId;
}
