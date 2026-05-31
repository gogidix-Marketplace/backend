package com.gogidix.digitalmarketing.seo.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateKeywordCommand {

    private String id;
    private String tenantId;
}
