package com.gogidix.hr.globalhrdashboard.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteMetricCategoryCommand {

    private String id;
    private String tenantId;
}
