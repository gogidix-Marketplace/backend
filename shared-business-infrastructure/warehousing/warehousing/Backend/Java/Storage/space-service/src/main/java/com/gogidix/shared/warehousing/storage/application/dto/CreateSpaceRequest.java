package com.gogidix.shared.warehousing.storage.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO for creating a new storage space
 */
@Data
public class CreateSpaceRequest {

    @NotBlank(message = "Tenant ID is required")
    private String tenantId;

    @NotBlank(message = "Space code is required")
    private String spaceCode;

    @NotBlank(message = "Space type is required")
    private String spaceType; // GENERAL, CLIMATE_CONTROLLED, BONDED, HAZARDOUS

    @NotNull(message = "Length is required")
    @Positive(message = "Length must be positive")
    private Double lengthMeters;

    @NotNull(message = "Width is required")
    @Positive(message = "Width must be positive")
    private Double widthMeters;

    @NotNull(message = "Height is required")
    @Positive(message = "Height must be positive")
    private Double heightMeters;

    @NotNull(message = "Base price is required")
    @Positive(message = "Base price must be positive")
    private BigDecimal basePricePerDay;

    @NotBlank(message = "Currency is required")
    private String currency = "USD";

    private String facilityZone;
    private String shelfLevel;
    private String binNumber;
}
