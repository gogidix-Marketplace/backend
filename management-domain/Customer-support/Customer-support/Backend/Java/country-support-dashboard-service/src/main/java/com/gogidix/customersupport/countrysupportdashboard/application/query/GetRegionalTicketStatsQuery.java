package com.gogidix.customersupport.countrysupportdashboard.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetRegionalTicketStatsQuery {

    private String tenantId;
    private String id;
}
