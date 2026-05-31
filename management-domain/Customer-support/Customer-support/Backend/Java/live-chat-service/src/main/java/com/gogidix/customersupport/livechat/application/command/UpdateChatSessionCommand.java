package com.gogidix.customersupport.livechat.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateChatSessionCommand {

    private String id;
    private String tenantId;
}
