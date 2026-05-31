package com.gogidix.shared.courier.driver.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Application DTO for driver profile
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverDTO {

    private String id;
    private String tenantId;
    private String driverId;
    private String fullName;
    private String email;
    private String phone;
    private String status;
    private List<Double> currentLocation; // [longitude, latitude]
    private String vehicleType;
    private String licensePlate;
    private Double rating;
    private Integer totalDeliveries;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
