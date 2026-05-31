package com.gogidix.corporate.website.infrastructure.external.lead;

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
public class LeadEventRequest {
    private String eventType;
    private String sessionId;
    private String leadId;
    private String pageUrl;
    private String referrer;
    private LocalDateTime timestamp;
    private Map<String, Object> metadata;
}
