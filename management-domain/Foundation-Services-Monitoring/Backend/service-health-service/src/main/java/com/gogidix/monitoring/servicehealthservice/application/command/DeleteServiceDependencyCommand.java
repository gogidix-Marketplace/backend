package com.gogidix.monitoring.servicehealthservice.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteServiceDependencyCommand {

    private String id;
    private String tenantId;
}
