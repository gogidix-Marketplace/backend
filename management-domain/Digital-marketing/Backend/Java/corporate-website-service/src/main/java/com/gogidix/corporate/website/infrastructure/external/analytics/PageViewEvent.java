package com.gogidix.corporate.website.infrastructure.external.analytics;

import com.gogidix.corporate.website.domain.model.Language;
import com.gogidix.corporate.website.domain.model.Region;
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
public class PageViewEvent {
    private String sessionId;
    private String pageUrl;
    private String pageTitle;
    private String referrer;
    private String userAgent;
    private Language language;
    private Region region;
    private String countryCode;
    private LocalDateTime timestamp;
    private Map<String, Object> metadata;
}
