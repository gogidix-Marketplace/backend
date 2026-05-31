package com.gogidix.customersupport.supportanalytics.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetTicketTrendQuery {

    private String tenantId;
    private String id;
}
