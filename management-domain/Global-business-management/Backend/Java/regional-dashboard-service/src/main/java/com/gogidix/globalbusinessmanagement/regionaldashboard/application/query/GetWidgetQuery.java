package com.gogidix.globalbusinessmanagement.regionaldashboard.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetWidgetQuery {

    private String tenantId;
    private String id;
}
