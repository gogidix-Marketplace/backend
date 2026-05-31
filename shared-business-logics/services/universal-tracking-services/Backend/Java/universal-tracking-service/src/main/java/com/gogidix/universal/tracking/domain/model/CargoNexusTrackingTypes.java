package com.gogidix.universal.tracking.domain.model;

public final class CargoNexusTrackingTypes {

    private CargoNexusTrackingTypes() {}

    public static final String TRACKING_TYPE_TANKER = "TANKER";
    public static final String TRACKING_TYPE_TRAILER = "TRAILER";
    public static final String TRACKING_TYPE_CONTAINER = "CONTAINER";

    public static final String TRACKING_MODE_GPS = "GPS";
    public static final String TRACKING_MODE_IOT = "IOT";
    public static final String TRACKING_MODE_SEAL = "SMART_SEAL";

    public static final String STATUS_LOADING = "LOADING";
    public static final String STATUS_IN_TRANSIT = "IN_TRANSIT";
    public static final String STATUS_AT_STOP = "AT_STOP";
    public static final String STATUS_DISCHARGING = "DISCHARGING";
    public static final String STATUS_COMPLETED = "COMPLETED";

    public static final String VMI_DELIVERY = "VMI_DELIVERY";
    public static final String MULTI_STOP_DELIVERY = "MULTI_STOP_DELIVERY";

    public static final String METRIC_FUEL_LEVEL = "fuel_level_litres";
    public static final String METRIC_TEMPERATURE = "temperature_celsius";
    public static final String METRIC_PRESSURE = "pressure_psi";
    public static final String METRIC_SPEED = "speed_kmh";
    public static final String METRIC_SEAL_STATUS = "seal_status";
}
