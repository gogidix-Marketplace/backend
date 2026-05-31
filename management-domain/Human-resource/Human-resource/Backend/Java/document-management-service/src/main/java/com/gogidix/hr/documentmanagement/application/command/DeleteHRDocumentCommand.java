package com.gogidix.hr.documentmanagement.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteHRDocumentCommand {

    private String id;
    private String tenantId;
}
