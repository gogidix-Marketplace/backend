package com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.domain.model;

public final class CargoNexusSLAPolicies {

    private CargoNexusSLAPolicies() {}

    public static final String DELIVERY_SLA = "CARGONEXUS_DELIVERY_SLA";
    public static final String INSURANCE_RESPONSE_SLA = "CARGONEXUS_INSURANCE_RESPONSE_SLA";
    public static final String PAYMENT_PROCESSING_SLA = "CARGONEXUS_PAYMENT_PROCESSING_SLA";
    public static final String LOADING_SLA = "CARGONEXUS_LOADING_SLA";
    public static final String BANK_PROCESSING_SLA = "CARGONEXUS_BANK_PROCESSING_SLA";

    public static final int DELIVERY_SLA_HOURS = 48;
    public static final int INSURANCE_RESPONSE_SLA_HOURS = 4;
    public static final int PAYMENT_PROCESSING_SLA_HOURS = 24;
    public static final int LOADING_SLA_HOURS = 8;
    public static final int BANK_PROCESSING_SLA_HOURS = 48;

    public static final String SEVERITY_CRITICAL = "CRITICAL";
    public static final String SEVERITY_HIGH = "HIGH";
    public static final String SEVERITY_MEDIUM = "MEDIUM";
    public static final String SEVERITY_LOW = "LOW";

    public static final String[] ALL_POLICIES = {
        DELIVERY_SLA, INSURANCE_RESPONSE_SLA, PAYMENT_PROCESSING_SLA,
        LOADING_SLA, BANK_PROCESSING_SLA
    };
}
