package com.gogidix.shared.warehousing.returns.interfaces.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Error response structure")
public class ErrorResponse {

    @Schema(description = "Timestamp when error occurred")
    private LocalDateTime timestamp;

    @Schema(description = "HTTP status code")
    private Integer status;

    @Schema(description = "Error type")
    private String error;

    @Schema(description = "Error message")
    private String message;

    @Schema(description = "Request path")
    private String path;

    @Schema(description = "Validation errors details")
    private Map<String, String> details;
}
