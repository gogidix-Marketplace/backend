package com.gogidix.customersupport.notification.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteNotificationCommand {

    private String id;
    private String tenantId;
}
