package com.gogidix.digitalmarketing.socialmedia.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateSocialAccountCommand {

    private String id;
    private String tenantId;
}
