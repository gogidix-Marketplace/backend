package com.gogidix.shared.courier.ecommerce.application.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.gogidix.shared.courier.ecommerce.domain.valueobject.VehicleType;

@Service
public class ZoneCourierPoolService {

    private static final Logger log = LoggerFactory.getLogger(ZoneCourierPoolService.class);

    public record CourierInfo(
        String courierId,
        String courierName,
        String courierPhone,
        VehicleType vehicleType
    ) {}

    public CourierInfo findAvailableCourier(String zoneId, VehicleType preferredVehicleType) {
        log.info("Finding available courier in zone {} with vehicle type {}", zoneId, preferredVehicleType);

        String courierId = "courier-" + UUID.randomUUID().toString().substring(0, 8);
        String courierName = "Zone-" + zoneId + "-Courier";

        return new CourierInfo(
            courierId,
            courierName,
            "+2348012345678",
            preferredVehicleType != null ? preferredVehicleType : VehicleType.MOTORCYCLE
        );
    }

    public Map<String, Integer> getAvailableVehicleTypes(String zoneId) {
        Map<String, Integer> types = new HashMap<>();
        types.put(VehicleType.MOTORCYCLE.name(), 10);
        types.put(VehicleType.VAN.name(), 3);
        types.put(VehicleType.TRUCK.name(), 2);
        return types;
    }

    public int getAvailableCourierCount(String zoneId) {
        return 15;
    }

    public String getAverageETA(String zoneId) {
        return "15 minutes";
    }

    public boolean isSurgePricing(String zoneId) {
        return false;
    }
}
