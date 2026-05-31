package com.gogidix.finance.conversion.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteDomainBaseEntityCommand {

    private String id;
    private String tenantId;
}
