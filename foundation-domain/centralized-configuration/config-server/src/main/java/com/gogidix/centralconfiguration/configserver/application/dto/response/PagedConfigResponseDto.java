package com.gogidix.centralconfiguration.configserver.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for paginated configuration response.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response DTO for paginated configuration results")
public class PagedConfigResponseDto<T> {

    @Schema(description = "List of items")
    private List<T> items;

    @Schema(description = "Current page number (0-indexed)")
    private Integer page;

    @Schema(description = "Page size")
    private Integer size;

    @Schema(description = "Total number of elements")
    private Long totalElements;

    @Schema(description = "Total number of pages")
    private Integer totalPages;

    @Schema(description = "Whether this is the first page")
    private Boolean isFirst;

    @Schema(description = "Whether this is the last page")
    private Boolean isLast;
}
