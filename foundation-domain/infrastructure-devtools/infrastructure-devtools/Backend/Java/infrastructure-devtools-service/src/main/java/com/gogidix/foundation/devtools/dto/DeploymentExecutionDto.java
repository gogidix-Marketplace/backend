package com.gogidix.foundation.devtools.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeploymentExecutionDto {

    private Long id;
    private UUID uuid;
    private Long jobId;
    private String status;
    private String version;
    private String commitSha;
    private String outputLog;
    private String errorLog;
    private Long startTime;
    private Long endTime;
    private Long duration;
    private String executedBy;
    private LocalDateTime executedAt;
}
