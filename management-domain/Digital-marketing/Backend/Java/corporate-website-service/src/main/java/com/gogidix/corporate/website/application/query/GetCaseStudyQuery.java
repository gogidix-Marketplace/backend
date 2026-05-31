package com.gogidix.corporate.website.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetCaseStudyQuery {

    private String tenantId;
    private String id;
}
