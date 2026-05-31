package com.gogidix.globalbusinessmanagement.datavalidation.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDataQualityReportCommand {

    private String id;
    private String tenantId;
}
