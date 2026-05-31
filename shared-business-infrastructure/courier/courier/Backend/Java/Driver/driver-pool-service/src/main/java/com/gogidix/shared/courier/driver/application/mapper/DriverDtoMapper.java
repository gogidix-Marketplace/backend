package com.gogidix.shared.courier.driver.application.mapper;

import com.gogidix.shared.courier.driver.application.dto.DriverDTO;
import com.gogidix.shared.courier.driver.application.command.CreateDriverCommand;
import com.gogidix.shared.courier.driver.application.command.UpdateDriverCommand;
import com.gogidix.shared.courier.driver.domain.entity.DriverProfile;
import com.gogidix.shared.courier.driver.domain.entity.DriverStatus;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Mapper for Driver entity and DTOs
 */
@Component
public class DriverDtoMapper {

    public DriverProfile toEntity(CreateDriverCommand command) {
        return DriverProfile.builder()
                .driverId(command.getDriverId())
                .fullName(command.getFullName())
                .email(command.getEmail())
                .phone(command.getPhone())
                .vehicleType(command.getVehicleType())
                .licensePlate(command.getLicensePlate())
                .status(DriverStatus.OFFLINE)
                .rating(0.0)
                .totalDeliveries(0)
                .build();
    }

    public void updateEntity(DriverProfile driver, UpdateDriverCommand command) {
        if (command.getFullName() != null) {
            driver.setFullName(command.getFullName());
        }
        if (command.getEmail() != null) {
            driver.setEmail(command.getEmail());
        }
        if (command.getPhone() != null) {
            driver.setPhone(command.getPhone());
        }
        if (command.getVehicleType() != null) {
            driver.setVehicleType(command.getVehicleType());
        }
        if (command.getLicensePlate() != null) {
            driver.setLicensePlate(command.getLicensePlate());
        }
    }

    public DriverDTO toDTO(DriverProfile driver) {
        return DriverDTO.builder()
                .id(driver.getId())
                .tenantId(driver.getTenantId())
                .driverId(driver.getDriverId())
                .fullName(driver.getFullName())
                .email(driver.getEmail())
                .phone(driver.getPhone())
                .status(driver.getStatus().name())
                .currentLocation(driver.getCurrentLocation() != null ?
                        List.of(driver.getCurrentLocation().getX(), driver.getCurrentLocation().getY()) : null)
                .vehicleType(driver.getVehicleType())
                .licensePlate(driver.getLicensePlate())
                .rating(driver.getRating())
                .totalDeliveries(driver.getTotalDeliveries())
                .createdAt(driver.getCreatedAt())
                .updatedAt(driver.getUpdatedAt())
                .build();
    }
}
