package com.gogidix.aiservices.aifrauddetectionservice.domain.model;

public final class CargoNexusFraudPatterns {

    private CargoNexusFraudPatterns() {}

    public static final String FUEL_THEFT = "CARGONEXUS_FUEL_THEFT";
    public static final String LOADING_FRAUD = "CARGONEXUS_LOADING_FRAUD";
    public static final String ROUTE_DIVERSION = "CARGONEXUS_ROUTE_DIVERSION";
    public static final String QUANTITY_MANIPULATION = "CARGONEXUS_QUANTITY_MANIPULATION";
    public static final String SEAL_TAMPERING = "CARGONEXUS_SEAL_TAMPERING";
    public static final String PAYMENT_FRAUD = "CARGONEXUS_PAYMENT_FRAUD";
    public static final String IDENTITY_FRAUD = "CARGONEXUS_IDENTITY_FRAUD";
    public static final String INSURANCE_CLAIM_FRAUD = "CARGONEXUS_INSURANCE_CLAIM_FRAUD";

    public static final String[] ALL_PATTERNS = {
        FUEL_THEFT, LOADING_FRAUD, ROUTE_DIVERSION, QUANTITY_MANIPULATION,
        SEAL_TAMPERING, PAYMENT_FRAUD, IDENTITY_FRAUD, INSURANCE_CLAIM_FRAUD
    };

    public static final double FUEL_THEFT_THRESHOLD = 0.5;
    public static final double ROUTE_DEVIATION_KM = 5.0;
    public static final double QUANTITY_DISCREPANCY_PERCENT = 2.0;
    public static final double SEAL_TAMPER_CONFIDENCE = 0.8;
}
