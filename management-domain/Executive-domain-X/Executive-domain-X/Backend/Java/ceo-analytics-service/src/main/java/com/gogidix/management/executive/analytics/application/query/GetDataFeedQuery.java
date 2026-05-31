package com.gogidix.management.executive.analytics.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetDataFeedQuery {

    private String tenantId;
    private String id;
}
