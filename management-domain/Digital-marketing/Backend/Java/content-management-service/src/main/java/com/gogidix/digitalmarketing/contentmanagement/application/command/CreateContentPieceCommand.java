package com.gogidix.digitalmarketing.contentmanagement.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateContentPieceCommand {

    private String id;
    private String tenantId;
}
