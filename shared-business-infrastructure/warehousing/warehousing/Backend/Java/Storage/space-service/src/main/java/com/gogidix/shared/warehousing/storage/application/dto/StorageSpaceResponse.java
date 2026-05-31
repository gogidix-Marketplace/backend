package com.gogidix.shared.warehousing.storage.application.dto;

import com.gogidix.shared.warehousing.storage.domain.entity.StorageSpace;
import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO for storage space response
 */
@Data
public class StorageSpaceResponse {

    private String id;
    private String tenantId;
    private String spaceCode;
    private String spaceType;
    private Double lengthMeters;
    private Double widthMeters;
    private Double heightMeters;
    private Double totalCapacityCubicMeters;
    private Double availableCapacityCubicMeters;
    private Integer availableSlots;
    private BigDecimal basePricePerDay;
    private String currency;
    private String facilityZone;
    private String shelfLevel;
    private String binNumber;
    private String status;
    private double utilizationPercentage;
    private boolean available;

    public static StorageSpaceResponse fromEntity(StorageSpace space) {
        StorageSpaceResponse response = new StorageSpaceResponse();
        response.setId(space.getId());
        response.setTenantId(space.getTenantId());
        response.setSpaceCode(space.getSpaceCode());
        response.setSpaceType(space.getSpaceType());
        response.setLengthMeters(space.getLengthMeters());
        response.setWidthMeters(space.getWidthMeters());
        response.setHeightMeters(space.getHeightMeters());
        response.setTotalCapacityCubicMeters(space.getTotalCapacityCubicMeters());
        response.setAvailableCapacityCubicMeters(space.getAvailableCapacityCubicMeters());
        response.setAvailableSlots(space.getAvailableSlots());
        response.setBasePricePerDay(space.getBasePricePerDay());
        response.setCurrency(space.getCurrency());
        response.setFacilityZone(space.getFacilityZone());
        response.setShelfLevel(space.getShelfLevel());
        response.setBinNumber(space.getBinNumber());
        response.setStatus(space.getStatus());
        response.setUtilizationPercentage(space.getUtilizationPercentage());
        response.setAvailable(space.isAvailable());
        return response;
    }
}
