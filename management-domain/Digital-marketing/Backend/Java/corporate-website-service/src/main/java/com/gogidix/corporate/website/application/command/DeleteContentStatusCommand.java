package com.gogidix.corporate.website.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteContentStatusCommand {

    private String id;
    private String tenantId;
}
