package com.gogidix.infrastructure.lockservice.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for lock acquisition response.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response for lock acquisition attempts")
public class LockAcquisitionResponseDto {

    @Schema(description = "Whether the lock was successfully acquired")
    private boolean acquired;

    @Schema(description = "The acquired lock (if successful)")
    private LockResponseDto lock;

    @Schema(description = "Error message if acquisition failed")
    private String errorMessage;

    @Schema(description = "When the acquisition attempt was made")
    private LocalDateTime attemptedAt;

    @Schema(description = "When the lock was acquired (if successful)")
    private LocalDateTime acquiredAt;

    @Schema(description = "Number of retries made during acquisition")
    private Integer retryCount;

    @Schema(description = "Whether the acquisition failed due to timeout")
    private boolean timedOut;
}
