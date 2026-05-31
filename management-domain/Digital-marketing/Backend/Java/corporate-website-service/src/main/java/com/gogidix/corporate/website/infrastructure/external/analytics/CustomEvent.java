package com.gogidix.corporate.website.infrastructure.external.analytics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomEvent {
    private String eventName;
    private String sessionId;
    private String category;
    private String action;
    private String label;
    private Double value;
    private LocalDateTime timestamp;
    private Map<String, Object> properties;
}
