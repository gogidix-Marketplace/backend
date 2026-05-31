package com.gogidix.courier.etaservice.shared.util;

/**
 * Vehicle type enumeration with associated base speeds.
 */
public enum VehicleType {
    WALKING("walking", 5.0, 3.0),
    BIKE("bike", 25.0, 15.0),
    MOTORCYCLE("motorcycle", 35.0, 20.0),
    CAR("car", 40.0, 25.0),
    TRUCK("truck", 30.0, 20.0);

    private final String code;
    private final double baseSpeedKmh; // Base speed in km/h
    private final double averageSpeedKmh; // Average speed in km/h

    VehicleType(String code, double baseSpeedKmh, double averageSpeedKmh) {
        this.code = code;
        this.baseSpeedKmh = baseSpeedKmh;
        this.averageSpeedKmh = averageSpeedKmh;
    }

    public String getCode() {
        return code;
    }

    public double getBaseSpeedKmh() {
        return baseSpeedKmh;
    }

    public double getAverageSpeedKmh() {
        return averageSpeedKmh;
    }

    /**
     * Get vehicle type from code.
     *
     * @param code the vehicle type code
     * @return the corresponding vehicle type
     */
    public static VehicleType fromCode(String code) {
        if (code == null || code.isBlank()) {
            return CAR; // Default to car
        }

        for (VehicleType type : values()) {
            if (type.code.equalsIgnoreCase(code)) {
                return type;
            }
        }

        return CAR; // Default to car if not found
    }
}
