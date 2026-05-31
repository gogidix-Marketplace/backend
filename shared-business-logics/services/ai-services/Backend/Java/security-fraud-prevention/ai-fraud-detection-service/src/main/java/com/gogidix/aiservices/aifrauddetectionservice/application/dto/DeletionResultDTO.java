package com.gogidix.aiservices.aifrauddetectionservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * DTO for deletion operation results.
 * Contains information about the result of a delete operation.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeletionResultDTO {

    private String deletedId;

    private String entityType;

    private boolean success;

    private String message;

    private Instant deletedAt;

    private String deletedBy;

    private String reason;
}
