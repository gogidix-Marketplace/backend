package com.gogidix.digitalmarketing.emailmarketing.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEmailSubscriberCommand {

    private String id;
    private String tenantId;
}
