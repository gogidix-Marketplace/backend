package com.gogidix.finance.conversion.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetDomainBaseEntityQuery {

    private String tenantId;
    private String id;
}
