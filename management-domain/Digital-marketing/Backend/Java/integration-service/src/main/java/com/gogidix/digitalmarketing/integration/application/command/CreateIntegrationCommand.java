package com.gogidix.digitalmarketing.integration.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateIntegrationCommand {

    private String id;
    private String tenantId;
}
