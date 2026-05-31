package com.gogidix.customersupport.knowledgebase.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetArticleQuery {

    private String tenantId;
    private String id;
}
