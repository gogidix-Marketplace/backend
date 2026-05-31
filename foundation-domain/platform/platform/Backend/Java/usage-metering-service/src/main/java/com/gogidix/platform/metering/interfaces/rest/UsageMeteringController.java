package com.gogidix.platform.metering.interfaces.rest;

import com.gogidix.platform.metering.application.dto.CreateUsageRecordRequestDto;
import com.gogidix.platform.metering.application.dto.UsageRecordDto;
import com.gogidix.platform.metering.application.service.UsageMeteringService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST controller for usage metering operations.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/usage")
@RequiredArgsConstructor
@Tag(name = "Usage Metering", description = "Usage tracking and metering APIs")
public class UsageMeteringController {

    private final UsageMeteringService usageMeteringService;

    @PostMapping
    @Operation(summary = "Record usage event", description = "Record a new usage event for metering")
    public ResponseEntity<UsageRecordDto> recordUsage(@Valid @RequestBody CreateUsageRecordRequestDto request) {
        log.info("Received usage record request: metric={}", request.getMetricName());
        UsageRecordDto result = usageMeteringService.recordUsage(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping
    @Operation(summary = "Get usage records", description = "Get usage records for the current tenant")
    public ResponseEntity<List<UsageRecordDto>> getUsage(
        @RequestParam(required = false) LocalDateTime startTime,
        @RequestParam(required = false) LocalDateTime endTime) {

        if (startTime != null && endTime != null) {
            return ResponseEntity.ok(usageMeteringService.getUsageByTimeRange(startTime, endTime));
        }
        return ResponseEntity.ok(usageMeteringService.getUsageByTenant());
    }

    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Check if the usage metering service is running")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Usage Metering Service is running");
    }
}
