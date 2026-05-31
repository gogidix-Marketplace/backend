package com.gogidix.digitalmarketing.brandmanagement.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteBrandGuidelineCommand {

    private String id;
    private String tenantId;
}
