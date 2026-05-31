package com.gogidix.infrastructure.lockservice.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for lock release response.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response for lock release operations")
public class LockReleaseResponseDto {

    @Schema(description = "Whether the release was successful")
    private boolean success;

    @Schema(description = "The lock that was released")
    private LockResponseDto releasedLock;

    @Schema(description = "Error message if release failed")
    private String errorMessage;

    @Schema(description = "When the release occurred")
    private LocalDateTime releasedAt;

    @Schema(description = "Whether the lock was already expired")
    private boolean wasExpired;
}
