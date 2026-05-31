package com.gogidix.sales.dashboard.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteKPIWidgetCommand {

    private String id;
    private String tenantId;
}
