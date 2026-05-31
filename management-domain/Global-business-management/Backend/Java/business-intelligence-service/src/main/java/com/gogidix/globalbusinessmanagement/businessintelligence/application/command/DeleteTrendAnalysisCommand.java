package com.gogidix.globalbusinessmanagement.businessintelligence.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteTrendAnalysisCommand {

    private String id;
    private String tenantId;
}
