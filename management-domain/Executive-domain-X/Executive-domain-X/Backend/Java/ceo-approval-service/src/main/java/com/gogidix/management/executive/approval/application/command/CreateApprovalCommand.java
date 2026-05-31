package com.gogidix.management.executive.approval.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateApprovalCommand {

    private String id;
    private String tenantId;
}
