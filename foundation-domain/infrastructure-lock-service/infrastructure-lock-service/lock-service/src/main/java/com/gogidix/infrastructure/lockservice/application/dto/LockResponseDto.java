package com.gogidix.infrastructure.lockservice.application.dto;

import com.gogidix.infrastructure.lockservice.domain.model.LockStatus;
import com.gogidix.infrastructure.lockservice.domain.model.LockType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for lock responses.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response containing lock information")
public class LockResponseDto {

    @Schema(description = "Unique identifier for this lock instance")
    private UUID lockId;

    @Schema(description = "Tenant identifier")
    private String tenantId;

    @Schema(description = "Resource being locked")
    private String resourceKey;

    @Schema(description = "Type of lock")
    private LockType lockType;

    @Schema(description = "Current status of the lock")
    private LockStatus status;

    @Schema(description = "Identifier of the entity holding this lock")
    private String holderId;

    @Schema(description = "Holder name")
    private String holderName;

    @Schema(description = "When the lock was acquired")
    private LocalDateTime acquiredAt;

    @Schema(description = "When the lock will expire")
    private LocalDateTime expiresAt;

    @Schema(description = "Remaining time to live in seconds")
    private Long remainingTtlSeconds;

    @Schema(description = "Number of retry attempts made")
    private Integer retryCount;

    @Schema(description = "Metadata associated with the lock")
    private String metadata;

    public static LockResponseDto fromDomain(com.gogidix.infrastructure.lockservice.domain.model.Lock lock) {
        return LockResponseDto.builder()
            .lockId(lock.getLockId())
            .tenantId(lock.getTenantId())
            .resourceKey(lock.getResourceKey())
            .lockType(lock.getLockType())
            .status(lock.getStatus())
            .holderId(lock.getHolderId())
            .holderName(lock.getHolderName())
            .acquiredAt(lock.getAcquiredAt())
            .expiresAt(lock.getExpiresAt())
            .remainingTtlSeconds(lock.getRemainingTtlSeconds())
            .retryCount(lock.getRetryCount())
            .metadata(lock.getMetadata())
            .build();
    }
}
