package com.gogidix.foundation.devtools.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * DTO for log entries.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LogEntryDto {

    private UUID uuid;
    private String level;
    private String source;
    private String category;
    private String message;
    private String stackTrace;
    private Map<String, Object> context;
    private String userId;
    private String sessionId;
    private String requestId;
    private LocalDateTime createdAt;
}
