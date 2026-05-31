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
 * Command to create a tracking session.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateSessionCommand {

    @NotBlank(message = "Session ID is required")
    private String sessionId;

    private String userId;

    private String source;

    private String ipAddress;

    private String userAgent;

    private String deviceType;

    private String browser;

    private String os;

    private String country;

    private String city;

    private String referrer;

    private String landingPage;

    private String campaign;

    private Map<String, Object> metadata;
}
