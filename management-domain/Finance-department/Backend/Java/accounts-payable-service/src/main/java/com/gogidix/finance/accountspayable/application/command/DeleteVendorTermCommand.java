package com.gogidix.finance.accountspayable.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteVendorTermCommand {

    private String id;
    private String tenantId;
}
