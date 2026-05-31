package com.gogidix.universal.tracking.domain.port.in;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Command to create a tracking event.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateEventCommand {

    @NotBlank(message = "Event type is required")
    private String eventType;

    private String sessionId;

    private String userId;

    private String source;

    @NotNull(message = "Timestamp is required")
    private LocalDateTime timestamp;

    private String eventName;

    private String description;

    private Map<String, Object> properties;

    private Map<String, Object> metadata;

    private String ipAddress;

    private String userAgent;

    private String referrer;

    private String pageUrl;

    private String pageTitle;

    private String correlationId;

    private Integer priority;
}
