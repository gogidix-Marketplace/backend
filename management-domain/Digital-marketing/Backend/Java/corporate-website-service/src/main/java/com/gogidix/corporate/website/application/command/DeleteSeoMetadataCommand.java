package com.gogidix.corporate.website.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteSeoMetadataCommand {

    private String id;
    private String tenantId;
}
