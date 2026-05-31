package com.gogidix.dashboard.aggregation.domain.port.in;

import lombok.Builder;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Input port command for creating an aggregation request.
 */
@Data
@Builder
public class CreateAggregationCommand {

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    @NotBlank(message = "Tenant ID is required")
    private String tenantId;

    @NotNull(message = "Source domains are required")
    private List<String> sourceDomains;

    private List<String> kpiCodes;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String aggregationType;

    private String groupBy;

    private String filters;

    private String createdBy;
}
