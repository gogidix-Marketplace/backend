package com.gogidix.customersupport.knowledgebase.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateArticleTagCommand {

    private String id;
    private String tenantId;
}
