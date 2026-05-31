package com.gogidix.sales.dealmanagement.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteDealCommand {

    private String id;
    private String tenantId;
}
