package com.gogidix.digitalmarketing.leadgeneration.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateLeadHandoffCommand {

    private String id;
    private String tenantId;
}
