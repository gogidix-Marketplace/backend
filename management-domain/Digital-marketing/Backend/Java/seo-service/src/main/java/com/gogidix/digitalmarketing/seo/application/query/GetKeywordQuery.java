package com.gogidix.digitalmarketing.seo.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetKeywordQuery {

    private String tenantId;
    private String id;
}
