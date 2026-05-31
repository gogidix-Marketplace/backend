package com.gogidix.shared.courier.dispatch.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Query object for searching dispatch orders
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DispatchQuery {

    private String dispatchId;

    private String orderId;

    private String customerId;

    private String driverId;

    private String status;

    private Integer minPriority;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Double nearLatitude;

    private Double nearLongitude;

    private Double radiusKm;

    private Integer page;

    private Integer size;

    private String sortBy;

    private String sortDirection;
}
