package com.gogidix.digitalmarketing.leadgeneration.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetLeadActivityQuery {

    private String tenantId;
    private String id;
}
