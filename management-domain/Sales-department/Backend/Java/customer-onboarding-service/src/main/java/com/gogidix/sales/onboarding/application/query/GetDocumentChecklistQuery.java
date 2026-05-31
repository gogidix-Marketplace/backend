package com.gogidix.sales.onboarding.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetDocumentChecklistQuery {

    private String tenantId;
    private String id;
}
