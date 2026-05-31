package com.gogidix.shared.courier.notification.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * DTO for notification preferences
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationPreferenceDTO {

    private String preferenceId;
    private String tenantId;
    private String userId;
    private Map<String, Boolean> channelPreferences;
    private Map<String, Boolean> typePreferences;
    private Boolean pushEnabled;
    private Boolean emailEnabled;
    private Boolean smsEnabled;
    private String timezone;
}
