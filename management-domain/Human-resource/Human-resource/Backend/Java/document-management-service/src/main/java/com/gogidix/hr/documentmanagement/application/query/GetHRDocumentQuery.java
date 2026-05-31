package com.gogidix.hr.documentmanagement.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetHRDocumentQuery {

    private String tenantId;
    private String id;
}
