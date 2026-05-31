package com.gogidix.shared.courier.dispatch.interfaces.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Dashboard statistics response
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {

    private Long totalDispatches;

    private Long pendingDispatches;

    private Long assignedDispatches;

    private Long inTransitDispatches;

    private Long deliveredDispatches;

    private Long cancelledDispatches;

    private Long failedDispatches;

    private Double averageDeliveryTimeMinutes;

    private Double averageDistanceKm;

    private Long activeDrivers;

    private Map<String, Long> dispatchesByStatus;

    private Map<String, Long> dispatchesByPriority;
}
