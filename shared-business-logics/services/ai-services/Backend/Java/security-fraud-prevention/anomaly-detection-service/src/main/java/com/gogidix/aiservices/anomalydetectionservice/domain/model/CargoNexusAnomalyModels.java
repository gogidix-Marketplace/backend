package com.gogidix.aiservices.anomalydetectionservice.domain.model;

public final class CargoNexusAnomalyModels {

    private CargoNexusAnomalyModels() {}

    public static final String FUEL_LEVEL_DROP = "CARGONEXUS_FUEL_LEVEL_DROP";
    public static final String SEAL_TAMPERING = "CARGONEXUS_SEAL_TAMPERING";
    public static final String GPS_DEVIATION = "CARGONEXUS_GPS_DEVIATION";
    public static final String TEMPERATURE_ANOMALY = "CARGONEXUS_TEMPERATURE_ANOMALY";
    public static final String PRESSURE_ANOMALY = "CARGONEXUS_PRESSURE_ANOMALY";
    public static final String LOADING_TIME_ANOMALY = "CARGONEXUS_LOADING_TIME_ANOMALY";
    public static final String DELIVERY_DELAY_ANOMALY = "CARGONEXUS_DELIVERY_DELAY_ANOMALY";

    public static final String[] ALL_MODELS = {
        FUEL_LEVEL_DROP, SEAL_TAMPERING, GPS_DEVIATION,
        TEMPERATURE_ANOMALY, PRESSURE_ANOMALY,
        LOADING_TIME_ANOMALY, DELIVERY_DELAY_ANOMALY
    };

    public static final String SENSOR_TYPE_FUEL_LEVEL = "FUEL_LEVEL_SENSOR";
    public static final String SENSOR_TYPE_GPS = "GPS_TRACKER";
    public static final String SENSOR_TYPE_SEAL = "SMART_SEAL";
    public static final String SENSOR_TYPE_TEMPERATURE = "TEMPERATURE_SENSOR";
    public static final String SENSOR_TYPE_PRESSURE = "PRESSURE_SENSOR";

    public static final double FUEL_DROP_RATE_THRESHOLD = 0.1;
    public static final double GPS_DEVIATION_THRESHOLD_KM = 2.0;
    public static final double TEMPERATURE_RANGE_MIN = -10.0;
    public static final double TEMPERATURE_RANGE_MAX = 50.0;
}
