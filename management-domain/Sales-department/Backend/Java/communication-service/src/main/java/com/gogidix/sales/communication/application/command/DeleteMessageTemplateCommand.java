package com.gogidix.sales.communication.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteMessageTemplateCommand {

    private String id;
    private String tenantId;
}
