package com.gogidix.digitalmarketing.emailmarketing.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetEmailMetricsQuery {

    private String tenantId;
    private String id;
}
