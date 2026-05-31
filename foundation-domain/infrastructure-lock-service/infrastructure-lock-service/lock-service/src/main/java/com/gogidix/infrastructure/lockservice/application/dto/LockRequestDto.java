package com.gogidix.infrastructure.lockservice.application.dto;

import com.gogidix.infrastructure.lockservice.domain.model.LockType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for lock acquisition requests.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to acquire a distributed lock")
public class LockRequestDto {

    @Schema(description = "Tenant identifier for multi-tenant isolation", required = true, example = "tenant-123")
    @NotBlank(message = "Tenant ID is required")
    private String tenantId;

    @Schema(description = "Resource being locked (e.g., 'order:123', 'user:456:profile')", required = true, example = "order:123")
    @NotBlank(message = "Resource key is required")
    private String resourceKey;

    @Schema(description = "Identifier of the entity requesting the lock", required = true, example = "service-instance-1")
    @NotBlank(message = "Holder ID is required")
    private String holderId;

    @Schema(description = "Optional holder name for easier identification", example = "OrderProcessingService")
    private String holderName;

    @Schema(description = "Type of lock to acquire", defaultValue = "EXCLUSIVE")
    private LockType lockType = LockType.EXCLUSIVE;

    @Schema(description = "Time to live for the lock in seconds", example = "300")
    @Positive(message = "TTL must be positive")
    private Long ttlSeconds;

    @Schema(description = "Maximum time to wait for lock acquisition in seconds", example = "30")
    @Positive(message = "Wait time must be positive")
    private Long waitTimeSeconds;

    @Schema(description = "Maximum number of retry attempts", example = "3")
    @Positive(message = "Max retries must be positive")
    private Integer maxRetries;

    @Schema(description = "Retry interval in milliseconds", example = "100")
    @Positive(message = "Retry interval must be positive")
    private Long retryIntervalMs;

    @Schema(description = "Metadata associated with the lock request", example = "{\"transactionId\": \"tx-123\"}")
    private String metadata;

    @Schema(description = "Whether to wait for lock if not immediately available", defaultValue = "false")
    private Boolean waitForLock = false;
}
