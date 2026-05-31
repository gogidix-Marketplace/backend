package com.gogidix.shared.courier.driver.application.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Query object for filtering driver searches
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverQuery {

    private String tenantId;

    private String status;

    private String vehicleType;

    private Double latitude;

    private Double longitude;

    private Double radiusKm;

    private Double minRating;

    private Boolean availableOnly;
}
