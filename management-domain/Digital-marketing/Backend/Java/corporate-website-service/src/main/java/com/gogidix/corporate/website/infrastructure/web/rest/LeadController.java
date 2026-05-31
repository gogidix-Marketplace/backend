package com.gogidix.corporate.website.infrastructure.web.rest;

import com.gogidix.corporate.website.application.dto.LeadCaptureRequest;
import com.gogidix.corporate.website.infrastructure.external.lead.LeadCaptureResponse;
import com.gogidix.corporate.website.infrastructure.external.lead.LeadGenerationClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/leads")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Lead Capture", description = "Lead capture and integration API")
public class LeadController {

    private final LeadGenerationClient leadGenerationClient;

    @Value("${application.integration.lead-generation.enabled:true}")
    private boolean leadIntegrationEnabled;

    @Value("${application.security.api-key}")
    private String apiKey;

    @PostMapping
    @Operation(summary = "Capture lead", description = "Capture a lead from website forms and forward to lead generation service")
    @ApiResponse(responseCode = "201", description = "Lead successfully captured")
    @ApiResponse(responseCode = "400", description = "Invalid request data")
    @ApiResponse(responseCode = "503", description = "Lead generation service unavailable")
    public ResponseEntity<LeadCaptureResponse> captureLead(
            @Valid @RequestBody LeadCaptureRequest request
    ) {
        log.info("Capturing lead for email: {}", request.getEmail());

        if (!leadIntegrationEnabled) {
            log.warn("Lead integration disabled, returning mock response");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(LeadCaptureResponse.builder()
                            .success(false)
                            .message("Lead capture service is currently disabled")
                            .build());
        }

        try {
            LeadCaptureResponse response = leadGenerationClient.captureLead(request, apiKey);
            if (response.isSuccess()) {
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
            }
        } catch (Exception e) {
            log.error("Failed to capture lead: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(LeadCaptureResponse.builder()
                            .success(false)
                            .message("Failed to process lead. Please try again later.")
                            .build());
        }
    }

    @GetMapping("/health")
    @Operation(summary = "Check lead service health", description = "Check if the lead generation service is available")
    @ApiResponse(responseCode = "200", description = "Service is healthy")
    public ResponseEntity<HealthResponse> checkHealth() {
        return ResponseEntity.ok(new HealthResponse(
                leadIntegrationEnabled ? "UP" : "DISABLED",
                leadIntegrationEnabled
        ));
    }

    public record HealthResponse(String status, boolean enabled) {}
}
