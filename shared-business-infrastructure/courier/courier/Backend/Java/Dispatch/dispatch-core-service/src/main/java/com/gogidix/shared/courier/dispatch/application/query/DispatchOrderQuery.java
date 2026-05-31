package com.gogidix.shared.courier.dispatch.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Query object for filtering dispatch order searches
 * Supports multiple filter criteria including geospatial queries
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DispatchOrderQuery {

    private String tenantId;

    private String orderId;

    private String customerId;

    private String status;

    private String priority;

    private String assignedDriverId;

    /**
     * Geospatial filters for nearby dispatches
     */
    private Double latitude;

    private Double longitude;

    private Double radiusKm;

    /**
     * Date range filters
     */
    private LocalDateTime createdAfter;

    private LocalDateTime createdBefore;

    private LocalDateTime pickupAfter;

    private LocalDateTime pickupBefore;

    private LocalDateTime deliveryAfter;

    private LocalDateTime deliveryBefore;

    /**
     * Only unassigned dispatches
     */
    private Boolean unassignedOnly;

    /**
     * Search term for partial matching
     */
    private String searchTerm;

    /**
     * Pagination
     */
    @Builder.Default
    private Integer page = 0;

    @Builder.Default
    private Integer size = 20;

    @Builder.Default
    private String sortBy = "createdAt";

    @Builder.Default
    private String sortDirection = "DESC";
}
