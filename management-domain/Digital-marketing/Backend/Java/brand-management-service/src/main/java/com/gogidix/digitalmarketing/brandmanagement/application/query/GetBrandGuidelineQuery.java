package com.gogidix.digitalmarketing.brandmanagement.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetBrandGuidelineQuery {

    private String tenantId;
    private String id;
}
