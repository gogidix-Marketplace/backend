package com.gogidix.digitalmarketing.analytics.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteMarketingMetricCommand {

    private String id;
    private String tenantId;
}
