package com.gogidix.corporate.website.infrastructure.external.lead;

import com.gogidix.corporate.website.application.dto.LeadCaptureRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "lead-generation-service", url = "${application.integration.lead-generation.base-url}")
@CircuitBreaker(name = "leadGenerationService")
@Retry(name = "leadGenerationService")
public interface LeadGenerationClient {

    @PostMapping("/api/v1/leads/capture")
    LeadCaptureResponse captureLead(
            @RequestBody LeadCaptureRequest request,
            @RequestHeader("X-API-Key") String apiKey
    );

    default LeadCaptureResponse captureLeadFallback(LeadCaptureRequest request, Throwable throwable) {
        return LeadCaptureResponse.builder()
                .success(false)
                .message("Lead capture service unavailable. Please try again later.")
                .leadId(null)
                .build();
    }

    @io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker(name = "leadGenerationService")
    @PostMapping("/api/v1/leads/track")
    void trackEvent(
            @RequestBody LeadEventRequest event,
            @RequestHeader("X-API-Key") String apiKey
    );
}
