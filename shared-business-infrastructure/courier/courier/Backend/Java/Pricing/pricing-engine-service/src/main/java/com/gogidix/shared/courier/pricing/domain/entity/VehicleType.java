package com.gogidix.shared.courier.pricing.domain.entity;

/**
 * Enumeration of vehicle types
 * Defines transport vehicle categories
 */
public enum VehicleType {
    /**
     * Bike/Motorcycle - for small parcels, documents
     */
    BIKE,

    /**
     * Car/Sedan - for medium-sized packages
     */
    CAR,

    /**
     * Van - for larger packages and multiple items
     */
    VAN,

    /**
     * Truck - for heavy and bulk items
     */
    TRUCK,

    /**
     * Electric Bike - eco-friendly option
     */
    E_BIKE,

    /**
     * Walking - for very short distances
     */
    WALKING
}
