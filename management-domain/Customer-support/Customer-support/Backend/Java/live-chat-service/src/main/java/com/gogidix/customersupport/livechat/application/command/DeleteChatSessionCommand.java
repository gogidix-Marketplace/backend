package com.gogidix.customersupport.livechat.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteChatSessionCommand {

    private String id;
    private String tenantId;
}
