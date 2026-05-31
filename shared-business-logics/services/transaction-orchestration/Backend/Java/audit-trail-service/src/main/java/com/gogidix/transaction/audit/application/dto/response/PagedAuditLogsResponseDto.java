package com.gogidix.transaction.audit.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for paged audit logs response.
 */
@Schema(description = "Paged audit logs response")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagedAuditLogsResponseDto {

    @Schema(description = "List of audit logs")
    private List<AuditLogResponseDto> auditLogs;

    @Schema(description = "Current page number")
    private Integer page;

    @Schema(description = "Page size")
    private Integer size;

    @Schema(description = "Total elements")
    private Long totalElements;

    @Schema(description = "Total pages")
    private Integer totalPages;

    @Schema(description = "Has next page")
    private Boolean hasNext;

    @Schema(description = "Has previous page")
    private Boolean hasPrevious;
}
