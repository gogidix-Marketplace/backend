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
public class DatabaseQueryExecutionDto {

    private Long id;
    private UUID uuid;
    private Long queryId;
    private String status;
    private Integer rowsAffected;
    private Integer rowsReturned;
    private String resultData;
    private String errorMessage;
    private Long executionTime;
    private String executedBy;
    private LocalDateTime executedAt;
}
