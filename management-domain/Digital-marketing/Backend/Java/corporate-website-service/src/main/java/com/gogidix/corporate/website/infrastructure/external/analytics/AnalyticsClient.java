package com.gogidix.corporate.website.infrastructure.external.analytics;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "analytics-service", url = "${application.integration.analytics.base-url}")
@CircuitBreaker(name = "analyticsService")
@Retry(name = "analyticsService")
public interface AnalyticsClient {

    @PostMapping("/api/v1/events/page-view")
    void trackPageView(
            @RequestBody PageViewEvent event,
            @RequestHeader("X-API-Key") String apiKey
    );

    @PostMapping("/api/v1/events/custom")
    void trackCustomEvent(
            @RequestBody CustomEvent event,
            @RequestHeader("X-API-Key") String apiKey
    );

    default void trackPageViewFallback(PageViewEvent event, Throwable throwable) {
        // Silently fail - analytics should not break the main application
    }

    default void trackCustomEventFallback(CustomEvent event, Throwable throwable) {
        // Silently fail - analytics should not break the main application
    }
}
