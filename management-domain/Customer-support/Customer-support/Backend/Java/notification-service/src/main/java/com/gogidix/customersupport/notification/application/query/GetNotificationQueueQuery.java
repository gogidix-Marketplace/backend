package com.gogidix.customersupport.notification.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetNotificationQueueQuery {

    private String tenantId;
    private String id;
}
