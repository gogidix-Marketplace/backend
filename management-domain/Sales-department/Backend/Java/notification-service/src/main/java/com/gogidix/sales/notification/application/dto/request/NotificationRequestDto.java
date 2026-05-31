package com.gogidix.sales.notification.application.dto.request;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequestDto {
    private String tenantId;
    private String recipientId;
    private String channel;
    private String subject;
    private String content;
    private String priority;
    private String templateId;
}
